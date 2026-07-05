const API =
"http://localhost:8081/payments";

let pendingTimers={};

function toast(msg){

const t=
document.getElementById(
"toast"
);

t.innerText=
msg;

t.classList.add(
"show"
);

setTimeout(()=>{

t.classList.remove(
"show"

);

},1800);

}

async function loadPayments(){

try{

const res=
await fetch(API);

if(
!res.ok
){

toast(
"Cannot load"
);

return;

}

const data=
await res.json();

if(
!Array.isArray(
data
)
){
return;
}

data.sort(
(a,b)=>
b.id-a.id
);

let html="";

for(
const p
of
data
){

// only pending auto success
if(
p.status==="PENDING"
&&
!pendingTimers[p.id]
){

pendingTimers[p.id]=
setTimeout(
async()=>{

try{

const check=
await fetch(
API+"/"+p.id
);

const current=
await check.json();

if(
current.status==="PENDING"
){

await fetch(
API+"/"+p.id,
{

method:
"PUT",

headers:{
"Content-Type":
"application/json"
},

body:
JSON.stringify({

status:
"SUCCESS",

retryCount:
current.retryCount

})

}

);

}

delete pendingTimers[p.id];

await loadPayments();

}
catch(e){

console.log(e);

}

},
10000
);

}

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

html+=`

<div class="card">

<h2>${p.orderId}</h2>

<p>
₹ ${p.amount}
</p>

<p class="${cls}">
${p.status}
</p>

<p class="retryText">
Retries:
${p.retryCount || 0}/2
</p>

${
p.status?.trim()==="FAILED"

?

`

<button
class="retryBtn"
onclick="retry(${p.id})">

↻ Retry

</button>

`

:

`

<button
class="retryBtn"
disabled>

✓ Completed

</button>

`

}

<button
class="deleteBtn"
onclick="deletePayment(${p.id})">

🗑 Delete

</button>

</div>

`;

}

document
.getElementById(
"payments"
)
.innerHTML=
html;

}
catch(e){

console.log(e);

toast(
"Error"
);

}

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

await fetch(
API,
{

method:
"POST",

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
"Created ✓"
);

await loadPayments();

}

async function retry(id){

try{

const res=
await fetch(
API+
"/"+
id+
"/retry",
{
method:
"PUT"
}
);

if(
!res.ok
){

toast(
"Retry failed"
);

return;

}

const updated=
await res.json();

toast(

updated.status==="SUCCESS"

?

"Recovered ✓"

:

`Retry ${updated.retryCount}/2`

);

await loadPayments();

}
catch{

toast(
"Retry failed"
);

}

}

async function deletePayment(id){

await fetch(
API+
"/"+
id,
{
method:
"DELETE"
}
);

toast(
"Deleted"
);

await loadPayments();

}

window.onload=()=>{

loadPayments();

setInterval(
loadPayments,
20000
);

};
