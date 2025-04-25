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
        <button id="close-btn">&times;</button>
        <h3>Login</h3>
        <label>Email</label>
        <input type="text" id="email" placeholder="Enter Email">
        <label>Password</label>
        <input type="password" id="password" placeholder="Enter password">
        <button id="submit-login" onclick="submitLogin()">Login</button>
        <p id="errorBlock"></p>
    `;

    document.body.appendChild(dialog);

    // Close button handler
    document.getElementById("close-btn").onclick = () => {
        dialog.remove();
        overlay.remove();
    };
}


function submitLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const formData = JSON.stringify({ emailId: email, password: password });

    const errorBlock = document.getElementById("errorBlock");

    fetch("http://192.168.100.99:8081/Altus_war_exploded/userLogin", {
        method: "POST",
        body: formData,
        credentials: 'include',
    })
        .then((response) => {
            let data = response.json().then(data => {
                if (response.status === 200) {
                    console.log("API response:", data);
                    errorBlock.className = "success";
                    errorBlock.innerHTML = data.message;
                    console.log(`cookie is ... ${response.headers.get('Set-Cookie')}`)
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

function logout() {
    fetch('/Altus_war_exploded/logout', {
        method: 'POST',
        credentials: 'include'
    })
        .then(() => {
            showLoginView();
        });
}

document.addEventListener('click', function (e) {
    const profile = document.querySelector('.profile-wrapper');
    const menu = document.getElementById('profile-menu');

    if (!profile.contains(e.target)) {
        menu?.classList.add('hidden');
    }
});

