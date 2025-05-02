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

    // Close button handler
    // document.getElementById("close-btn").onclick = () => {
    //     dialog.remove();
    //     overlay.remove();
    // };
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
                    errorBlock.className = "success";
                    errorBlock.innerHTML = data.message;
                    console.log(`cookie is ... ${response.headers.get('Set-Cookie')}`)
                    closeLogin();
                    // loadCustomers();
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

function loadCustomers() {
    let table = document.getElementById('user-table');
    fetch("http://192.168.100.99:8081/Altus_war_exploded/getAllUsers", {
        method: 'GET', credentials: 'include', headers: {
            'Content-Type': 'application/json'
        }
    }).then((response) => {
        let data = response.json().then(data => {
            if (response.status === 200) {
                console.log("API response:", data);
                let table = document.getElementById('user-table');
                for (let user of data.data) {
                    let row = document.createElement('tr');
                    row.innerHTML = `
                    <td> ${user.displayId}</td>
                    <td> ${user.firstName}</td>
                    <td> ${user.lastName}</td>
                    <td> ${user.phoneNo}</td>
                    <td> ${user.emailId}</td>
                    <td> ${user.totalRatings}</td>
                    <td> ${user.active}</td>
                    <td> ${user.blocked}</td>
                    `;
                    table.append(row);
                }


            } else {
                console.log("API ERROR:", data);
                let table = document.getElementById('user-table');
                let row = document.createElement('tr');
                row.innerHTML = `
                    <td> ${data.message}</td>
                    `;
                table.append(row);

            }
        });

    })
        .catch((error) => {
            console.error("API error:", error);
        });
}


function init() {
    dashboard();
    login();
}