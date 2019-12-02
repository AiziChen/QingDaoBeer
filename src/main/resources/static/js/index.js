let fPhone = document.querySelector("#phone");
let fCode = document.querySelector("#verify_code");
let imgCode = document.querySelector("#verify_code_image");
let btn_add = document.querySelector("#btn_add");

btn_add.addEventListener("click", evt => {
    evt.preventDefault();
    let pn = fPhone.value;
    let code = fCode.value;
    axios.post("/phone/pushPhone?phone=" + pn + "&code=" + code)
        .then(res => {
            let data = res.data;
            alert(data.msg);
        });
});

imgCode.addEventListener("click", evt => {
    evt.preventDefault();
    imgCode.src = "/getVerify?" + Math.random();
});