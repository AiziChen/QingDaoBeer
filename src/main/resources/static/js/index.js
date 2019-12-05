let fPhone = document.querySelector("#phone");
let fCode = document.querySelector("#verify-code");

const btnAdd = document.querySelector("#btn-add");
btnAdd.addEventListener("click", evt => {
    evt.preventDefault();
    let pn = fPhone.value;
    let code = fCode.value;
    let base_url = "/phone/pushPhone?";
    if (cbIsDel.checked) {
        base_url = "/phone/rmPhone?"
    }
    axios.post(base_url + "phone=" + pn + "&code=" + code)
        .then(res => {
            let data = res.data;
            if (data.status === 1) {
                imgCode.src = "/getVerify?" + Math.random();
            }
            alert(data.msg);
        });
});

const imgCode = document.querySelector("#verify-code-image");
imgCode.addEventListener("click", evt => {
    evt.preventDefault();
    imgCode.src = "/getVerify?" + Math.random();
});

const cbIsDel = document.querySelector("#is-del");
cbIsDel.addEventListener("click", evt => {
    if (cbIsDel.checked) {
        btnAdd.value = "取消自动挂机";
    } else {
        btnAdd.value = "添加自动挂机";
    }
});