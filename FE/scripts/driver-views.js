function loadDriverDashboard() {
    driverDashboardView();

    createDriverSidebar();

    closeLogin();

}

function driverDashboardView() {
    const container = document.getElementById("page-container");
    container.innerHTML = '';
    container.innerHTML = `<div class="dashboard-wrapper">
        <div class="header">
            <h1>Welcome to the Dashboard</h1>
        </div>
        <div class="card-grid">
            <div class="card">
                <h3>Total Users</h3>
                <p>1,245</p>
            </div>
            <div class="card">
                <h3>New Signups</h3>
                <p>85 Today</p>
            </div>
            <div class="card">
                <h3>Active Sessions</h3>
                <p>312</p>
            </div>
            <div class="card">
                <h3>Revenue</h3>
                <p>$12,530</p>
            </div>
        </div>
    </div>`;
}

function createDriverSidebar() {
    const sidebar = document.createElement("aside");
    sidebar.className = "sidebar";

    const title = document.createElement("h2");
    title.textContent = "Driver";
    sidebar.appendChild(title);

    const menuItems = [{label: "Dashboard", action: "driverDashboardView"}, {
        label: "Vehicles",
        action: "vehicles"
    }, {label: "Locations", action: "locations"}, {label: "Rides", action: "rides"}, {
        label: "My Profile",
        action: "showUserFromLocalStorage"
    }];

    menuItems.forEach(item => {
        const p = document.createElement("p");
        p.textContent = item.label;
        p.setAttribute("onclick", `${item.action}()`);
        sidebar.appendChild(p);
    });

    document.body.prepend(sidebar);
}

function vehicles() {
    const container = document.getElementById("page-container");

    container.innerHTML = '';
    // Outer wrapper div
    const wrapper = document.createElement("div");
    wrapper.id = `table-wrapper`;
    wrapper.style.marginBottom = "40px";

    // Title
    const heading = document.createElement("h2");
    heading.textContent = "Add Your Vehicle";
    wrapper.appendChild(heading);

    const formFields = [{label: "brandModelId", type: "select", options: []},
        {label: "registrationNumber", type: "text"},
        {label: "year", type: "number"},
        {label: "fuelType", type: "select", options: ["PETROL", "DIESEL", "CNG", "ELECTRIC", "HYBRID"]},
        {label: "transmission", type: "select", options: ["MANUAL", "AUTOMATIC"]},
        {label: "groundClearance", type: "number"},
        {label: "wheelBase", type: "number"}];

    const form = makeTypedForm("vehicle", formFields);
    getAllBrandModels();
    wrapper.appendChild(form);
    container.appendChild(wrapper);
}

function getAllBrandModels() {
    fetch("http://192.168.100.99:8081/Altus_war_exploded/getAllModel", {
        method: "GET", credentials: "include", headers: {
            "Content-Type": "application/json",
        }
    }).then(value => {
        let data = value.json().then(data => {
            console.log(data.data);
            let modelDropdown = document.getElementById("brandModelId");

            modelDropdown.innerHTML = `<option value="">Select Model</option>`;

            data.data.forEach(item => {
                let option = document.createElement("option");
                option.value = item.brandModelId;
                option.textContent = `${item.brandName} - ${item.model}`;
                modelDropdown.appendChild(option);
            });

        })
    });

}

function showUserFromLocalStorage() {
    const container = document.getElementById("page-container");
    const userData = JSON.parse(localStorage.getItem("user")); // assuming stored with key 'userData'

    if (!userData) {
        container.innerHTML = "<p>No user data found in localStorage.</p>";
        return;
    }

    const content = `
        <div class="user-card">
            <h2>User Details</h2>
            <p><strong>Display ID:</strong> ${userData.displayId}</p>
            <p><strong>Name:</strong> ${userData.firstName} ${userData.lastName}</p>
            <p><strong>Email:</strong> ${userData.emailId}</p>
            <p><strong>Phone:</strong> ${userData.phoneNo}</p>
            <p><strong>Role:</strong> ${userData.role?.roleType || 'N/A'}</p>
            <p><strong>Total Ratings:</strong> ${userData.totalRatings}</p>
            <p><strong>Active:</strong> ${userData.active ? "Yes" : "No"}</p>
            <p><strong>Blocked:</strong> ${userData.blocked ? "Yes" : "No"}</p>
        </div>
        <div class="user-card">
            <h2>Upload Your Licence Here</h2>
            <form class="styled-form" id="licenseForm" enctype="multipart/form-data">
              <label for="licenceNumber">License Number:</label><br>
              <input type="text" id="licenceNumber" name="licenceNumber" required><br><br>
            
              <label for="licensePhoto">Upload License Photo:</label><br>
              <input type="file" id="licensePhoto" name="licensePhoto" accept="image/*" required><br><br>
            
              <button class="form-submit" type="submit">Submit License</button>
            </form>
            
            <div id="responseMessage"></div>

        </div>
    `;

    container.innerHTML = content;
    document.getElementById('licenseForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const form = event.target;
        const formData = new FormData();

        const licenseNo = document.getElementById('licenceNumber').value;
        const licensePhoto = document.getElementById('licensePhoto').files[0];

        formData.append('licenceNumber', licenseNo);
        formData.append('licencePhoto', licensePhoto);

        fetch('http://192.168.100.99:8081/Altus_war_exploded/addDriverDetails', {
            method: 'PATCH',
            credentials: 'include',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                document.getElementById('responseMessage').innerText = data.message || 'Submitted successfully!';
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('responseMessage').innerText = 'Submission failed!';
            });
    });


}