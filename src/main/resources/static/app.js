const API=
"http://localhost:8081/payments";

function toast(message){

const t=
document.getElementById(
"toast"
);

t.innerText=
message;

t.classList.add(
"show"
);

setTimeout(()=>{

t.classList.remove(
"show"
);

},2000);

}

async function loadPayments(){

const res=
await fetch(API);

let data=
await res.json();

data.sort(
(a,b)=>
b.id-a.id
);

let html="";

data.forEach(p=>{

let cls=
p.status==="SUCCESS"
?
"success"
:
p.status==="FAILED"
?
"failed"
:
"pending";

html += `

<div class="card">

<h2>${p.orderId}</h2>

<p>₹ ${p.amount}</p>

<p class="${cls}">
${p.status}
</p>

<p class="retryText">
Retries: ${p.retryCount}
</p>

<button
class="retryBtn"
onclick="retry(${p.id})">

↻ Retry Payment

</button>

<button
class="deleteBtn"
onclick="deletePayment(${p.id})">

🗑 Delete

</button>

</div>

`;

});

document
.getElementById(
"payments"
)
.innerHTML=
html;

}

async function createPayment(){

const payment={

orderId:
orderId.value,

amount:
Number(
amount.value
),

status:
status.value,

retryCount:0

};
console.log(payment);

await fetch(
API,
{

method:"POST",

headers:{
"Content-Type":
"application/json"
},

body:
JSON.stringify(
payment
)

}

);

toast(
"Payment Created ✓"
);

setTimeout(
loadPayments,
300
);

}

async function deletePayment(id){

await fetch(
API+"/"+id,
{
method:"DELETE"
}
);

toast("Deleted");

await loadPayments();

}

async function retry(id){

const res =
await fetch(
API+"/"+id+"/retry",
{
method:"PUT"
}
);

const updated =
await res.json();

if(updated.status==="PENDING"){

toast(
"Only FAILED payments can retry"
);

}
else{

toast(
`Recovered ✓ Retry ${updated.retryCount}`
);

}

await loadPayments();

}

loadPayments();