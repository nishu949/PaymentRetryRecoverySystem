const API_URL = window.location.origin;


async function loadDashboard() {

    try {

        const response = await fetch(`${API_URL}/dashboard`);
        const data = await response.json();

        document.getElementById("totalPayments").innerText = data.totalPayments;
        document.getElementById("successfulPayments").innerText = data.successfulPayments;
        document.getElementById("failedPayments").innerText = data.failedPayments;
        document.getElementById("recoveredPayments").innerText = data.recoveredPayments;

        createCharts(data);

    } catch (error) {

        console.error(error);

    }

}


function showToast(message){

    document.getElementById("toastMessage").innerHTML = message;

    const toast =
    new bootstrap.Toast(
        document.getElementById("liveToast")
    );

    toast.show();

}



async function loadPayments() {

    console.log("loadPayments() called");

    try {

        const response = await fetch(`${API_URL}/payments`);
        const payments = await response.json();

        const search = document
            .getElementById("searchInput")
            .value
            .toLowerCase()
            .trim();

        const status = document
            .getElementById("statusFilter")
            .value;

        const filteredPayments = payments.filter(payment => {

            const matchesSearch =

                payment.orderId.toLowerCase().includes(search) ||

                payment.status.toLowerCase().includes(search) ||

                payment.failureReason.toLowerCase().includes(search) ||

                payment.amount.toString().includes(search);

            const matchesStatus =
                status === "" || payment.status === status;

            return matchesSearch && matchesStatus;
        });

        renderPayments(filteredPayments);

    } catch (e) {

        console.error(e);

    }
}
function renderPayments(payments){

    
    console.log("Rendering", payments.length);

    const table = document.getElementById("paymentTable");

    table.innerHTML = "";

    if(payments.length === 0){
        table.innerHTML = `
        <tr>
            <td colspan="7" class="text-center">
                No Payments Found
            </td>
        </tr>`;
        return;
    }

    payments.forEach(payment => {
        let rowClass = "";

switch(payment.status){

    case "SUCCESS":
        rowClass = "success-row";
        break;

    case "FAILED":
        rowClass = "failed-row";
        break;

    case "PENDING":
        rowClass = "pending-row";
        break;

    case "RECOVERED":
        rowClass = "recovered-row";
        break;

    case "PERMANENT_FAILURE":
        rowClass = "permanent-row";
        break;
}

        table.innerHTML += `
      <tr class="${rowClass}">

            <td>${payment.id}</td>

            <td>${payment.orderId}</td>

            <td>₹ ${payment.amount}</td>

            <td>${statusBadge(payment.status)}</td>

            <td>${payment.failureReason || "NONE"}</td>

            <td>${payment.retryCount}</td>

            <td>

                ${
                    payment.status === "FAILED"
                    ? `
                    <button class="btn btn-outline-primary btn-sm"
                        onclick="retryPayment(${payment.id})">
                        Retry
                    </button>
                    `
                    : `
                    <button class="btn btn-outline-success btn-sm" disabled>
                        Retry
                    </button>
                    `
                }

                <button
                    class="btn btn-outline-danger btn-sm"
                    onclick="history(${payment.id})">
                    History
                </button>

                <button
                    class="btn btn-danger btn-sm"
                    onclick="deletePayment(${payment.id})">
                    Delete
                </button>

            </td>

        </tr>
        `;

    });

}
function statusBadge(status){

switch(status){

case "SUCCESS":

return `<span class="badge bg-success">SUCCESS</span>`;

case "FAILED":

return `<span class="badge bg-danger">FAILED</span>`;

case "RECOVERED":

return `<span class="badge bg-primary">RECOVERED</span>`;

case "PERMANENT_FAILURE":

return `<span class="badge bg-dark">PERMANENT</span>`;

default:

return `<span class="badge bg-warning">PENDING</span>`;

}

}

async function retryPayment(id){

    const btn = event.target;

    btn.disabled = false;
    btn.innerHTML = "Retrying...";
    btn.classList.remove("btn-primary");
    btn.classList.add("btn-warning");

    const response = await fetch(`${API_URL}/payments/${id}/retry`,{
        method:"PUT"
    });

    const payment = await response.json();

    btn.classList.remove("btn-warning");
    btn.classList.add("btn-success");
    btn.innerHTML = "Done";

    showToast(`Payment Status : ${payment.status}`);

    loadDashboard();
    loadPayments();
}

async function deletePayment(id){

    if(!confirm("Are you sure you want to delete this payment?")){
        return;
    }

    await fetch(`${API_URL}/payments/${id}`,{
        method:"DELETE"
    });

    showToast("Payment Deleted Successfully!");

    loadDashboard();
    loadPayments();
}

async function history(id){

const response =
await fetch(`${API_URL}/payments/${id}/history`);

const histories =
await response.json();

const div =
document.getElementById("historyContainer");

div.innerHTML="";

if(histories.length===0){

div.innerHTML="<h5>No Retry History</h5>";

}

histories.forEach(h=>{

div.innerHTML+=`

<div class="card mb-3">

<div class="card-body">

<h6>

Retry #${h.retryNumber}

</h6>

<p>

Status :
${h.status}

</p>

<p>

Reason :
${h.failureReason}

</p>

<p>

${h.message}

</p>

<small class="text-muted">
${new Date(h.retryTime).toLocaleString()}
</small>

</div>

</div>

`;

});

new bootstrap.Modal(

document.getElementById("historyModal")

).show();

}

document

.getElementById("paymentForm")

.addEventListener("submit",

async function(e){

e.preventDefault();

const payment={

orderId:

document.getElementById("orderId").value,

amount:

parseFloat(

document.getElementById("amount").value

),

status:

document.getElementById("status").value,

failureReason:

document.getElementById("failureReason").value,

retryCount:0

};

await fetch(

`${API_URL}/payments`,

{

method:"POST",

headers:{

"Content-Type":"application/json"

},

body:JSON.stringify(payment)

}

);

bootstrap.Modal

.getInstance(

document.getElementById("paymentModal")

).hide();

this.reset();

loadDashboard();

loadPayments();
showToast("Payment Created Successfully!");

});

let paymentChart;
let recoveryChart;

function createCharts(data) {

    // Payment Bar Chart
    if (paymentChart) {
        paymentChart.destroy();
    }

    paymentChart = new Chart(
document.getElementById("paymentChart"),
{
type:"bar",

data:{
labels:[
"Success",
"Failed",
"Recovered",
"Permanent"
],

datasets:[{
label:"Payments",
data:[
data.successfulPayments,
data.failedPayments,
data.recoveredPayments,
data.permanentFailures
]
}]
},

options:{
responsive:true,
maintainAspectRatio:false
}

});

if (recoveryChart) {
    recoveryChart.destroy();
}
    recoveryChart = new Chart(
document.getElementById("recoveryChart"),
{

type:"doughnut",

data:{
labels:[
"Recovered",
"Others"
],

datasets:[{
data:[
data.recoveredPayments,
data.totalPayments-data.recoveredPayments
]
}]
},

options:{
responsive:true,
maintainAspectRatio:false
}

});


}
document.getElementById("searchInput")
    .addEventListener("keyup", loadPayments);

document.getElementById("statusFilter")
    .addEventListener("change", loadPayments);
loadDashboard();
loadPayments();




const dashboardSection = document.getElementById("dashboardSection");
const analyticsSection = document.getElementById("analyticsSection");
const paymentSection = document.getElementById("paymentSection");

document.getElementById("dashboardMenu").onclick = function () {

    dashboardSection.scrollIntoView({
        behavior: "smooth"
    });

};

document.getElementById("paymentsMenu").onclick = function () {

    paymentSection.scrollIntoView({
        behavior: "smooth"
    });

};

document.getElementById("createMenu").onclick = function () {

    new bootstrap.Modal(
        document.getElementById("paymentModal")
    ).show();

};

document.getElementById("analyticsMenu").onclick = function () {

    analyticsSection.scrollIntoView({
        behavior: "smooth"
    });

};

document.getElementById("historyMenu").onclick = function () {

    paymentSection.scrollIntoView({
        behavior: "smooth"
    });

};



function exportCSV() {
    fetch(`${API_URL}/payments`)
        .then(response => response.json())
        .then(payments => {

            let csv = "ID,Order ID,Amount,Status,Failure Reason,Retry Count\n";

            payments.forEach(p => {
                csv += `${p.id},${p.orderId},${p.amount},${p.status},${p.failureReason},${p.retryCount}\n`;
            });

            const blob = new Blob([csv], { type: "text/csv" });
            const url = window.URL.createObjectURL(blob);

            const a = document.createElement("a");
            a.href = url;
            a.download = "payments.csv";
            a.click();

            window.URL.revokeObjectURL(url);
        });
}

let rowClass = "";

switch(payment.status){
    case "SUCCESS":
        rowClass = "success-row";
        break;
    case "FAILED":
        rowClass = "failed-row";
        break;
    case "PENDING":
        rowClass = "pending-row";
        break;
    case "RECOVERED":
        rowClass = "recovered-row";
        break;
}

setInterval(() => {
    loadDashboard();
    loadPayments();
}, 5000);

const search = document
    .getElementById("searchInput")
    .value
    .trim()
    .toLowerCase();

const filteredPayments = payments.filter(payment => {

    return (

        payment.orderId.toLowerCase().includes(search) ||

        payment.status.toLowerCase().includes(search) ||

        (payment.failureReason || "")
            .toLowerCase()
            .includes(search) ||

        payment.amount.toString().toLowerCase().includes(search)

    ) && (

        status === "" || payment.status === status

    );

});