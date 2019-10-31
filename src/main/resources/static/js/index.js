let fPhone = document.querySelector("#phone");
let btn_add = document.querySelector("#btn_add");

btn_add.addEventListener("click", evt => {
    evt.preventDefault();
    let pn = fPhone.value;
    axios.post("/phone/pushPhone?phone=" + pn).then( res => {
        let data = res.data;
        alert(data.msg);
    });
});
