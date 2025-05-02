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

    const menuItems = [
        { label: "Dashboard", action: "driverDashboardView" },
        { label: "Vehicles", action: "vehicles" },
        { label: "Locations", action: "locations" },
        { label: "Rides", action: "rides" },
        { label: "My Profile", action: "driverProfile" }
    ];

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
    let formFields = ["brandModelId", "registrationNumber", "year", "fuelType", "transmission", "groundClearance", "wheelBase"];
    const form = makeForm("vehicle", formFields);
    wrapper.appendChild(form);
    container.appendChild(wrapper);
}