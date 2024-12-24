// this function is used for  showing sidebar and hide sidebar

const toggleSidebar = ()=>{
    if($(".sidebar").is(":visible")){
        // toggle band krne ke liye
        $(".sidebar").css("display","none");

        $(".content").css("margin-left","0%");
    }
    else{   
        // toggle show krne ke liye
        $(".sidebar").css("display","block");

        $(".content").css("margin-left","20%");
    }
};
// search algorithm 

const search =() => {
  let query=$("#search-input").val();
  if (query=="") {
    $(".search-result").hide();
  } else {
    console.log(query);

    // sending request to server 
    let url=`http://localhost:3030/search/${query}`;

    fetch(url)
    .then((response) =>{
        return response.json();
    })
    .then((data) => {
        console.log(data);

        let text=`<div class='list-group'>`;

        data.forEach((member) => {
            text +=`<a  href='/user/${member.memid}/member' class='list-group-item list-group-action'> ${member.name} </a> `
        });
        text+=`</div>`;
       
        $(".search-result").html(text);
        $(".search-result").show();
    });
    
  }
};


// Set billingDate input to current date

window.addEventListener('DOMContentLoaded', (event) => {
  const dateInput = document.querySelector('input[name="billingDate"]');
  const today = new Date().toISOString().split('T')[0];
  dateInput.value = today;
});


const searchbills =() => {
  let query=$("#search-input").val();
  if (query=="") {
    $(".search-result").hide();
  } else {
    console.log(query);

    // sending request to server 
    let url=`http://localhost:3030/search-bill/${query}`;

    fetch(url)
    .then((response) =>{
        return response.json();
    })
    .then((data) => {
        console.log(data);

        let text=`<div class='list-group'>`;

        data.forEach((bill) => {
            text +=`<a  href='/user/${bill.billid}/bill' class='list-group-item list-group-action'> ${bill.name} </a> `
        });
        text+=`</div>`;
       
        $(".search-result").html(text);
        $(".search-result").show();
    });
    
  }
};

