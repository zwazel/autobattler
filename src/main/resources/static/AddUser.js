const usernameField = document.getElementById('username')
const passwordField = document.getElementById('password')

async function addUser() {
    const username = usernameField.value;
    const password = passwordField.value;
    console.log(username)
    console.log(password)
    const response = await fetch("/user/add", {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: JSON.stringify({'username': username, 'password': password})
    });
}