const API = "http://skillsphere-production-2f02.up.railway.app/api";
let currentUser = null;
let currentStudent = null;
let allPortalSkills = [];
let mySkills = [];
let myProjects = [];

const $ = id => document.getElementById(id);

async function api(path, options = {}) {
    const response = await fetch(API + path, {
        headers: { "Content-Type": "application/json", ...(options.headers || {}) },
        ...options
    });
    const text = await response.text();
    let data = null;
    try { data = text ? JSON.parse(text) : null; } catch { data = text; }
    if (!response.ok) {
        const message = typeof data === "string" ? data : (data?.message || data?.error || "Request failed");
        throw new Error(message);
    }
    return data;
}

function escapeHtml(v) {
    return String(v ?? "").replace(/[&<>"']/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"}[c]));
}

function showMessage(id, message, error = false) {
    const el = $(id);
    if (!el) return;
    el.textContent = message;
    el.classList.toggle("error", error);
}

function showLogin() {
    $("authArea").classList.remove("hidden");
    $("login").classList.remove("hidden");
    $("register").classList.add("hidden");
    $("appHeader").classList.add("hidden");
    $("app").classList.add("hidden");
}

function showRegister() {
    $("login").classList.add("hidden");
    $("register").classList.remove("hidden");
    showMessage("registerMessage", "");
}

function showApp() {
    $("authArea").classList.add("hidden");
    $("appHeader").classList.remove("hidden");
    $("app").classList.remove("hidden");
    showView("dashboard");
}

function showView(name) {
    document.querySelectorAll(".view").forEach(view => {
        view.classList.toggle("hidden", view.id !== `view-${name}`);
    });
    document.querySelectorAll("nav a[data-view]").forEach(link => {
        link.classList.toggle("active", link.dataset.view === name);
    });
    if (location.hash !== `#${name}`) history.replaceState(null, "", `#${name}`);
    window.scrollTo({top:0, behavior:"smooth"});
}

async function register(e) {
    e.preventDefault();
    const name = $("regName").value.trim();
    const email = $("regEmail").value.trim();
    const password = $("regPassword").value;
    const confirm = $("regConfirmPassword").value;
    if (password !== confirm) {
        showMessage("registerMessage", "Passwords do not match.", true);
        return;
    }
    try {
        await api("/auth/register", {
            method:"POST",
            body:JSON.stringify({name, email, password, role:"STUDENT"})
        });
        $("registerForm").reset();
        $("loginEmail").value = email;
        showLogin();
        showMessage("authMessage", "Account created successfully. Please login.");
    } catch (err) {
        showMessage("registerMessage", err.message, true);
    }
}

async function login(e) {
    e.preventDefault();
    try {
        currentUser = await api("/auth/login", {
            method:"POST",
            body:JSON.stringify({email:$("loginEmail").value.trim(), password:$("loginPassword").value})
        });
        localStorage.setItem("skillsphereUserId", currentUser.id);
        $("loginPassword").value = "";
        showMessage("authMessage", "");
        showApp();
        await loadData();
    } catch (err) {
        showMessage("authMessage", "Invalid email or password.", true);
    }
}

async function loadData() {
    try {
        const [students, skills] = await Promise.all([api("/students"), api("/skills")]);
        allPortalSkills = skills;
        $("studentCount").textContent = students.length;
        $("skillCount").textContent = skills.length;
        currentStudent = students.find(s => s.userId === currentUser.id) || null;

        if (!currentStudent) {
            await api("/students", {method:"POST", body:JSON.stringify({userId:currentUser.id,name:currentUser.email.split("@")[0]})});
            const refreshed = await api("/students");
            currentStudent = refreshed.find(s => s.userId === currentUser.id) || null;
        }

        fillProfile();
        await Promise.all([loadMySkills(), loadMyProjects()]);
        $("projectCount").textContent = (await api("/projects")).length;
        $("dashboardName").textContent = currentStudent?.name || currentUser.email.split("@")[0];
        populateSkillSelect();
    } catch (err) {
        showMessage("appMessage", "Could not load data: " + err.message, true);
    }
}

function fillProfile() {
    if (!currentStudent) return;
    $("studentId").value = currentStudent.id;
    $("studentName").value = currentStudent.name || "";
    $("studentContact").value = currentStudent.contact || "";
    $("studentBio").value = currentStudent.bio || "";
}

function populateSkillSelect() {
    $("skillSelect").innerHTML = `<option value="">Select existing skill</option>` +
        allPortalSkills.map(s => `<option value="${s.id}" data-name="${escapeHtml(s.name)}" data-category="${escapeHtml(s.category || "")}">${escapeHtml(s.name)}</option>`).join("");
}

async function loadMySkills() {
    if (!currentStudent) return;
    mySkills = await api(`/students/${currentStudent.id}/skills`);
    $("mySkillCount").textContent = mySkills.length;
    renderMySkills();
}

function renderMySkills() {
    const q = $("mySkillSearch").value.trim().toLowerCase();
    const list = mySkills.filter(s => (s.name || "").toLowerCase().includes(q) || (s.category || "").toLowerCase().includes(q));
    $("mySkillList").innerHTML = list.length ? list.map(s => `
        <article class="card">
            <h3>${escapeHtml(s.name)}</h3>
            <p>${escapeHtml(s.category || "General")}</p>
            <span class="skill-pill">${escapeHtml(s.level || "Not specified")}</span>
            <div class="actions"><button class="small danger" onclick="removeMySkill(${s.id})">Remove</button></div>
        </article>`).join("") : `<div class="empty">No skills added yet. Add your first skill above.</div>`;
}

async function saveStudent(e) {
    e.preventDefault();
    try {
        await api(`/students/${currentStudent.id}`, {
            method:"PUT",
            body:JSON.stringify({userId:currentUser.id,name:$("studentName").value.trim(),contact:$("studentContact").value.trim(),bio:$("studentBio").value.trim()})
        });
        const students = await api("/students");
        currentStudent = students.find(s => s.userId === currentUser.id);
        $("dashboardName").textContent = currentStudent.name;
        showMessage("appMessage", "Profile saved successfully.");
    } catch (err) { showMessage("appMessage", err.message, true); }
}

async function saveSkill(e) {
    e.preventDefault();
    try {
        let skillId = $("skillSelect").value;
        const newName = $("skillName").value.trim();
        const category = $("skillCategory").value.trim();
        if (!skillId) {
            if (!newName) throw new Error("Enter a skill name.");
            const existing = allPortalSkills.find(s => s.name.toLowerCase() === newName.toLowerCase());
            if (existing) skillId = existing.id;
            else {
                const created = await api("/skills", {method:"POST", body:JSON.stringify({name:newName,category})});
                skillId = created.id;
                allPortalSkills.push(created);
                populateSkillSelect();
            }
        }
        await api(`/students/${currentStudent.id}/skills`, {
            method:"POST",
            body:JSON.stringify({skillId:Number(skillId),level:$("skillLevel").value})
        });
        e.target.reset();
        $("skillLevel").value = "Intermediate";
        await loadMySkills();
        showMessage("appMessage", "Skill saved successfully.");
    } catch (err) { showMessage("appMessage", err.message, true); }
}

async function removeMySkill(skillId) {
    if (!confirm("Remove this skill from your profile?")) return;
    try {
        await api(`/students/${currentStudent.id}/skills/${skillId}`, {method:"DELETE"});
        await loadMySkills();
    } catch (err) { showMessage("appMessage", err.message, true); }
}

async function loadMyProjects() {
    if (!currentStudent) return;
    myProjects = await api(`/students/${currentStudent.id}/projects`);
    $("projectCount").textContent = myProjects.length;
    renderProjects();
}

function renderProjects() {
    const q = $("projectSearch").value.trim().toLowerCase();
    const list = myProjects.filter(p => (p.title || "").toLowerCase().includes(q) || (p.description || "").toLowerCase().includes(q));
    $("projectList").innerHTML = list.length ? list.map(p => `
        <article class="card">
            <h3>${escapeHtml(p.title)}</h3>
            <p>${escapeHtml(p.description || "No description provided.")}</p>
            ${p.githubUrl ? `<a href="${escapeHtml(p.githubUrl)}" target="_blank" rel="noopener">View GitHub</a>` : ""}
            <div class="actions"><button class="small danger" onclick="removeMyProject(${p.id})">Remove</button></div>
        </article>`).join("") : `<div class="empty">No projects added yet. Add your first project above.</div>`;
}

async function saveProject(e) {
    e.preventDefault();
    try {
        const project = await api("/projects", {
            method:"POST",
            body:JSON.stringify({title:$("projectTitle").value.trim(),description:$("projectDescription").value.trim(),githubUrl:$("projectGithub").value.trim()})
        });
        await api(`/students/${currentStudent.id}/projects/${project.id}`, {method:"POST"});
        e.target.reset();
        await loadMyProjects();
        showMessage("appMessage", "Project added successfully.");
    } catch (err) { showMessage("appMessage", err.message, true); }
}

async function removeMyProject(projectId) {
    if (!confirm("Remove this project from your profile?")) return;
    try {
        await api(`/students/${currentStudent.id}/projects/${projectId}`, {method:"DELETE"});
        await loadMyProjects();
    } catch (err) { showMessage("appMessage", err.message, true); }
}

async function searchStudentsBySkill() {
    const skill = $("studentSkillSearch").value.trim();
    if (!skill) {
        $("studentSearchResults").innerHTML = `<div class="empty">Enter a skill to search for students.</div>`;
        $("searchSummary").textContent = "";
        return;
    }
    try {
        const results = await api(`/students/search?skill=${encodeURIComponent(skill)}`);
        $("searchSummary").textContent = `${results.length} student${results.length === 1 ? "" : "s"} found with “${skill}”.`;
        $("studentSearchResults").innerHTML = results.length ? results.map(s => `
            <article class="student-card">
                <h3>${escapeHtml(s.name)}</h3>
                <div class="student-meta">${escapeHtml(s.contact || "Contact not provided")}</div>
                <p>${escapeHtml(s.bio || "No bio provided.")}</p>
                <div class="skill-pills">${(s.skills || []).map(x => `<span class="skill-pill">${escapeHtml(x)}</span>`).join("")}</div>
            </article>`).join("") : `<div class="empty">No students found for this skill. Try another skill name.</div>`;
    } catch (err) {
        showMessage("appMessage", "Search failed: " + err.message, true);
    }
}

// Navigation

document.querySelectorAll("[data-view]").forEach(el => {
    el.addEventListener("click", event => {
        event.preventDefault();
        showView(el.dataset.view);
    });
});

$("showRegisterBtn").addEventListener("click", showRegister);
$("backToLoginBtn").addEventListener("click", showLogin);
$("registerForm").addEventListener("submit", register);
$("loginForm").addEventListener("submit", login);
$("studentForm").addEventListener("submit", saveStudent);
$("skillForm").addEventListener("submit", saveSkill);
$("projectForm").addEventListener("submit", saveProject);
$("mySkillSearch").addEventListener("input", renderMySkills);
$("projectSearch").addEventListener("input", renderProjects);
$("studentSkillSearchBtn").addEventListener("click", searchStudentsBySkill);
$("studentSkillSearch").addEventListener("keydown", e => { if (e.key === "Enter") searchStudentsBySkill(); });
$("skillSelect").addEventListener("change", () => {
    const option = $("skillSelect").selectedOptions[0];
    if (option && option.value) {
        $("skillName").value = option.dataset.name || "";
        $("skillCategory").value = option.dataset.category || "";
    }
});
$("logoutBtn").addEventListener("click", () => {
    currentUser = null;
    currentStudent = null;
    localStorage.removeItem("skillsphereUserId");
    $("loginForm").reset();
    showLogin();
});

async function restoreSession() {
    const id = localStorage.getItem("skillsphereUserId");
    if (!id) return showLogin();
    try {
        currentUser = await api(`/auth/user/${id}`);
        showApp();
        await loadData();
        const hash = location.hash.replace("#", "");
        if (["dashboard","profile","skills","projects","find-students"].includes(hash)) showView(hash);
    } catch {
        localStorage.removeItem("skillsphereUserId");
        showLogin();
    }
}

restoreSession();
