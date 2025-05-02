function toggleProfileMenu() {
    const menu = document.getElementById('profile-menu');
    menu.classList.toggle('hidden');
}

function login() {
    // Blur the background
    let overlay = document.createElement('div');
    overlay.id = 'blur-overlay';
    document.body.appendChild(overlay);

    // Create dialog container
    let dialog = document.createElement('div');
    dialog.id = 'login-dialog';
    dialog.innerHTML = `
<!--        <button id="close-btn">&times;</button>-->
        <h3>Login</h3>
        <label>Email</label>
        <input type="text" id="email" placeholder="Enter Email">
        <label>Password</label>
        <input type="password" id="login-password" placeholder="Enter password">
        <button id="submit-login" onclick="submitLogin()">Login</button>
        <p id="errorBlock"></p>
    `;

    document.body.appendChild(dialog);

}


function submitLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("login-password").value;

    const formData = JSON.stringify({emailId: email, password: password});

    const errorBlock = document.getElementById("errorBlock");

    fetch("http://192.168.100.99:8081/Altus_war_exploded/userLogin", {
        method: "POST", body: formData, credentials: 'include',
    })
        .then((response) => {
            let data = response.json().then(data => {
                if (response.status === 200) {
                    console.log("API response:", data);

                    localStorage.setItem("user", JSON.stringify(data.data))
                    handleLoginResponse(data);
                    closeLogin();
                } else {
                    console.log("API ERROR:", data);
                    errorBlock.className = "error";
                    errorBlock.innerHTML = data.message;
                }
            });

        })
        .catch((error) => {
            console.error("API error:", error);
            errorBlock.className = "error";
            errorBlock.innerHTML = "Network Error !!!";
            errorBlock.hidden = false;
        });
}

function closeLogin() {
    let overlay = document.getElementById('blur-overlay');
    let dialogue = document.getElementById('login-dialog');
    if (overlay) overlay.remove();
    if (dialogue) dialogue.remove();
}


function viewProfile() {
    alert("View Profile clicked");
    // Replace with loadView('profile-view.html') if needed
}

function openSettings() {
    alert("Settings clicked");
    // Replace with loadView('settings-view.html') if needed
}


document.addEventListener('click', function (e) {
    const profile = document.querySelector('.profile-wrapper');
    const menu = document.getElementById('profile-menu');

    if (!profile.contains(e.target)) {
        menu?.classList.add('hidden');
    }
});

function init() {
    // loadAdminDashboard();
    login();
}

function createAdminSidebar() {
    const sidebar = document.createElement("aside");
    sidebar.className = "sidebar";

    const title = document.createElement("h2");
    title.textContent = "Admin";
    sidebar.appendChild(title);

    const menuItems = [
        { label: "Dashboard", action: "dashboard" },
        { label: "Customers", action: "customers" },
        { label: "Drivers", action: "drivers" },
        { label: "Services", action: "services" },
        { label: "Vehicles", action: "vehicles" },
        { label: "Locations", action: "locations" },
        { label: "Rides", action: "rides" }
    ];

    menuItems.forEach(item => {
        const p = document.createElement("p");
        p.textContent = item.label;
        p.setAttribute("onclick", `${item.action}()`);
        sidebar.appendChild(p);
    });

    document.body.prepend(sidebar); // or use a wrapper container if preferred
}

function handleLoginResponse(responseData) {
    const roleType = responseData.data.role.roleType;

    // Clear previous role classes if needed
    document.body.classList.remove("role-admin", "role-driver");

    // Apply theme based on role
    if (roleType === "ROLE_DRIVER") {
        document.body.classList.add("role-driver");
    } else if (roleType === "ROLE_ADMIN") {
        document.body.classList.add("role-admin"); // Optional if admin is default
    }

    // Load respective dashboard
    if (roleType === "ROLE_DRIVER") {
        loadDriverDashboard();
    } else {
        loadAdminDashboard();
    }
}

