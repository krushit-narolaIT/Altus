function loadAdminDashboard() {
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

    createAdminSidebar();

}

function customers() {
    const dataID = "user";
    const formFields = ["firstName", "lastName", "phoneNo", "emailId", "password"];
    const tableFields = ["firstName", "lastName", "phoneNo", "emailId", "totalRatings", "active", "blocked"];
    const dataFields = ["displayId", ...tableFields];

    createFormAndTable({
        title: "User List", formFields, tableFields, dataID
    });
    let dataURL = "http://192.168.100.99:8081/Altus_war_exploded/getAllUsers";

    attachFormHandler({
        dataID,
        submitUrl: "http://192.168.100.99:8081/Altus_war_exploded/userSignUp",
        formFields,
        tableFields: dataFields,
        dataURL
    });

    loadData(dataID, url = dataURL, dataFields);
}

function locations() {
    const dataID = "location";
    const formFields = ["name"];
    const tableFields = ["name", "actions"];
    const dataFields = ["id", ...tableFields];

    createFormAndTable({
        title: "Locations List", formFields, tableFields, dataID
    });

    let url = "http://192.168.100.99:8081/Altus_war_exploded/getAllLocations";
    attachFormHandler({
        dataID,
        submitUrl: "http://192.168.100.99:8081/Altus_war_exploded/addLocation",
        formFields,
        tableFields: dataFields,
        dataURL: url
    });

    loadData(dataID, url, dataFields);
}

function drivers() {
    const dataID = "driver";
    const formFields = ["firstName", "lastName", "phoneNo", "emailId", "password"];
    const tableFields = ["firstName", "lastName", "phoneNo", "emailId", "documentVerified", "licenceNumber", "licencePhoto"];
    const dataFields = ["displayId", ...tableFields];

    createFormAndTable({
        title: "Drivers List", formFields, tableFields, dataID
    });

    let url = "http://192.168.100.99:8081/Altus_war_exploded/getAllDrivers";
    attachFormHandler({
        dataID,
        submitUrl: "http://192.168.100.99:8081/Altus_war_exploded/driverSignUp",
        formFields,
        tableFields: dataFields,
        dataURL: url
    });

    loadData(dataID, url, dataFields);
}

function services() {
    const dataID = "service";
    const formFields = [
        {label: "serviceName", type: "text"},
        {label: "baseFare", type: "number"},
        {label: "perKmRate", type: "number"},
        {label: "vehicleType", type: "select", options: ["TWO_WHEELER", "FOUR_WHEELER", "THREE_WHEELER"]},
        {label: "maxPassengers", type: "number"},
        {label: "commissionPercentage", type: "number"}
    ];

    const tableFields = ["serviceName", "baseFare", "perKmRate", "vehicleType", "maxPassengers", "commissionPercentage"];
    const dataFields = ["serviceId", ...tableFields];

    createFormAndTable({
        title: "Services List", formFields, tableFields, dataID
    });
    let dataURL = "http://192.168.100.99:8081/Altus_war_exploded/getAllVehicleServices";

    attachFormHandler({
        dataID,
        submitUrl: "http://192.168.100.99:8081/Altus_war_exploded/addVehicleService",
        formFields: formFields.map(field => field.label),
        tableFields: dataFields,
        dataURL
    });

    loadData(dataID, url = dataURL, dataFields);
}


function makeForm(dataID, formFields) {
    const form = document.createElement("form");
    form.id = `${dataID}-form`;
    form.classList.add("styled-form");

    formFields.forEach((field) => {
        const fieldWrapper = document.createElement("div");
        fieldWrapper.classList.add("form-field");

        const label = document.createElement("label");
        label.textContent = field;
        label.setAttribute("for", field);

        const input = document.createElement("input");
        input.id = field;
        input.name = field;
        input.type = /password/i.test(field) ? "password" : "text";

        fieldWrapper.appendChild(label);
        fieldWrapper.appendChild(input);
        form.appendChild(fieldWrapper);
    });

    const button = document.createElement("button");
    button.type = "submit";
    button.textContent = "Submit";
    button.classList.add("form-submit");

    form.appendChild(button);
    return form;
}

function makeTypedForm(dataID, formFields) {
    const form = document.createElement("form");
    form.id = `${dataID}-form`;
    form.classList.add("styled-form");

    formFields.forEach((field) => {
        const fieldWrapper = document.createElement("div");
        fieldWrapper.classList.add("form-field");

        const label = document.createElement("label");
        label.textContent = field.label;
        label.setAttribute("for", field.label);
        let input;
        if (field.type === "select") {
            input = document.createElement("select");
            input.id = field.label;
            input.name = field.label;
            input.type = field.type;
            field.options.forEach(optionText => {
                const option = document.createElement("option");
                option.value = optionText;
                option.textContent = optionText;
                input.appendChild(option);
            });
        } else {
            input = document.createElement("input");
            input.id = field.label;
            input.name = field.label;
            input.type = field.type;
            input.step = "0.01";
        }
        fieldWrapper.appendChild(label);
        fieldWrapper.appendChild(input);
        form.appendChild(fieldWrapper);
    });

    const button = document.createElement("button");
    button.type = "submit";
    button.textContent = "Submit";
    button.classList.add("form-submit");

    form.appendChild(button);
    return form;
}

function createFormAndTable({title, formFields, tableFields, dataID}) {
    const container = document.getElementById("page-container");

    container.innerHTML = '';
    // Outer wrapper div
    const wrapper = document.createElement("div");
    wrapper.id = `table-wrapper`;
    wrapper.style.marginBottom = "40px";

    // Title
    const heading = document.createElement("h2");
    heading.textContent = title;
    wrapper.appendChild(heading);

    // Form
    let form;
    if (dataID === "service")
        form = makeTypedForm(dataID, formFields);
    else form = makeForm(dataID, formFields);

    wrapper.appendChild(form);

    // Table
    const table = document.createElement("table");
    table.id = `${dataID}-table`;
    table.classList.add("styled-table");

    const thead = table.createTHead();
    const headRow = thead.insertRow();

    // Insert numbered column as first header
    const thNumber = document.createElement("th");
    thNumber.textContent = "#";
    headRow.appendChild(thNumber);

    tableFields.forEach((field) => {
        const th = document.createElement("th");
        th.textContent = field;
        headRow.appendChild(th);
    });

    const tbody = document.createElement("tbody");
    table.appendChild(tbody);

    wrapper.appendChild(table);
    container.appendChild(wrapper);
}

function attachFormHandler({dataID, submitUrl, formFields, tableFields, dataURL}) {
    const form = document.getElementById(`${dataID}-form`);
    const errorBlock = document.getElementById("errorBlock");

    if (!form) {
        console.error(`Form with id '${dataID}' not found.`);
        return;
    }

    form.addEventListener("submit", (event) => {
        event.preventDefault();

        const dataObj = {};
        formFields.forEach(field => {
            const input = document.getElementById(field);
            dataObj[field] = input ? input.value : "";
        });

        const formData = JSON.stringify(dataObj);

        fetch(submitUrl, {
            method: "POST", headers: {"Content-Type": "application/json"}, credentials: "include", body: formData,
        }).then(response => response.json().then(data => {
            if (response.ok) {
                Toastify({
                    text: data.message || "Success!", duration: 3000, position: "center", // `left`, `center` or `right`
                    stopOnFocus: true, // Prevents dismissing of toast on hover
                    style: {
                        background: "linear-gradient(to right, #00b09b, #96c93d)",
                    },
                }).showToast();
                loadData(dataID, dataURL, tableFields);
            } else {
                Toastify({
                    text: data.message, duration: 3000, position: "center", // `left`, `center` or `right`
                    stopOnFocus: true, // Prevents dismissing of toast on hover
                    style: {
                        background: "linear-gradient(to right, #b71c1c, #f44336)",
                    },
                }).showToast();
            }
            form.reset();
        }))
            .catch((error) => {
                console.error("API error:", error);
                Toastify({
                    text: "Network Error", duration: 3000, position: "center", stopOnFocus: true, style: {
                        background: "linear-gradient(to right, #b71c1c, #f44336)",
                    },

                }).showToast();
            });
    });
}


function loadData(dataID, url, tableFields) {
    fetch(url, {
        method: "GET", credentials: "include", headers: {
            "Content-Type": "application/json",
        },
    }).then((response) => {
        let data = response.json().then((data) => {
            if (response.status === 200) {
                console.log("API response:", data);

                let table = document.getElementById(`${dataID}-table`);
                if (!table) return;

                let tbody = table.querySelector("tbody");
                if (!tbody) {
                    // If tbody doesn't exist, create one
                    tbody = document.createElement("tbody");
                    table.appendChild(tbody);
                }

                tbody.innerHTML = ""; // Clear existing rows

                for (let user of data.data) {
                    let row = document.createElement("tr");
                    tableFields.forEach((field) => {
                        const cell = document.createElement("td");
                        const value = user[field];

                        if (field === "documentVerified") {
                            const button = document.createElement("button");
                            button.className = "verify-btn";
                            button.textContent = value ? "Verified" : "Verify";
                            button.disabled = value; // Disable if already verified
                            button.addEventListener("click", () => {
                                console.log(`Verifying ${field} for:`, user);
                                alert("upload document feature pending");
                                // verifyDriverDocuments(driverId = user.id);

                            });
                            cell.appendChild(button);
                        } else if (field === "actions" && dataID === "location") {
                            const deleteBtn = document.createElement("button");
                            deleteBtn.className = "delete-btn";
                            deleteBtn.innerHTML = "&times;"; // Unicode ×

                            deleteBtn.addEventListener("click", () => {
                                console.log("Deleting:", user);
                                alert("feature pending");
                                deleteLocation(locationId);
                            });

                            cell.appendChild(deleteBtn);
                        } else {
                            cell.textContent = value !== undefined ? value : "-";
                        }

                        row.appendChild(cell);
                    });
                    tbody.appendChild(row);
                }
            } else {
                let table = document.getElementById(`${dataID}-table`);
                console.log("API ERROR:", data);
                let row = document.createElement("tr");
                row.innerHTML = `
                    <td> ${data.message}</td>
                    `;
                table.append(row);
            }
        });
    }).catch((error) => {
        console.error("API error:", error);
    });
}

// TODO: function deleteLocation(locationId) {
//     let url = `http://192.168.100.99:8081/Altus_war_exploded/deleteLocation?locationId=${locationId}`
//     fetch(url, {
//         method: "DELETE", headers: {"Content-Type": "application/json"}, credentials: "include",
//     }).then(response => response.json().then(data => {
//         if (response.ok) {
//             Toastify({
//                 text: data.message || "Success!", duration: 3000, position: "center", // `left`, `center` or `right`
//                 stopOnFocus: true, // Prevents dismissing of toast on hover
//                 style: {
//                     background: "linear-gradient(to right, #00b09b, #96c93d)",
//                 },
//             }).showToast();
//             loadData(dataID, dataURL, tableFields);
//         } else {
//             Toastify({
//                 text: data.message, duration: 3000, position: "center", // `left`, `center` or `right`
//                 stopOnFocus: true, // Prevents dismissing of toast on hover
//                 style: {
//                     background: "linear-gradient(to right, #b71c1c, #f44336)",
//                 },
//             }).showToast();
//         }
//     })).catch((error) => {
//         console.error("API error:", error);
//         Toastify({
//             text: "Network Error", duration: 3000, position: "center", stopOnFocus: true, style: {
//                 background: "linear-gradient(to right, #b71c1c, #f44336)",
//             },
//
//         }).showToast();
//     });
// }