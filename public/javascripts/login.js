
function pasuser(form) {
    if (form.id.value=="JavaScript") { 
        if (form.pass.value=="Kit") {              
            location="page2.html" 
        } else {
            alert("Invalid Password")
        }
        } else {  alert("Invalid UserID")
    }
}