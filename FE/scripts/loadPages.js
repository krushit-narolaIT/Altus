function dashboard() {
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
    const tableID = "location";
    const formFields = ["name"];
    const tableFields = ["name"];
    const dataFields = ["id", ...tableFields];

    createFormAndTable({
        title: "Locations List", formFields, tableFields, tableID
    });

    loadData(tableID, "http://192.168.100.99:8081/Altus_war_exploded/getAllLocations", dataFields);
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
    const form = document.createElement("form");
    form.id = `${dataID}-form`;
    form.style.marginBottom = "10px";

    formFields.forEach((field) => {
        const label = document.createElement("label");
        label.textContent = field;
        label.style.marginRight = "6px";

        const input = document.createElement("input");
        input.id = field;
        input.type = "text";
        input.style.marginRight = "20px";

        form.appendChild(label);
        form.appendChild(input);
    });

    const button = document.createElement("button");
    button.type = "submit";
    button.textContent = "Submit";
    form.appendChild(button);

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
            method: "POST", headers: {"Content-Type": "application/json"}, credentials: "same-origin", body: formData,
        })
            .then(response => response.json().then(data => {
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
    })
        .then((response) => {
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
                            cell.textContent = user[field];
                            row.appendChild(cell);
                        });
                        tbody.appendChild(row);
                    }
                } else {
                    console.log("API ERROR:", data);
                    let table = document.getElementById(dataID);
                    let row = document.createElement("tr");
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
