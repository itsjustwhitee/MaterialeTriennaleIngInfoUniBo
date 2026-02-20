function loadPage()
{
    this.loadCart();
    this.loadItems();
}

function loadCart()
{
    fetch('Shop?act=get&what=cart', { method: 'GET'})
    .then(response =>{
        console.log(response);
        return response.text();
    })
    .then(rawText => {
        return JSON.parse(rawText);
    })
    .then(data => {
        if(data != 'empty' && data != 'error')
        {
            this.updateCart(data.data);
        }
    })
    .catch(error => {
        console.log("Error (fetch): ", error);
    })
}

function checkout()
{
    fetch('Shop?act=checkout&what=cart', { method: 'GET'})
    .then(response =>{
        console.log(response);
        
        this.loadPage();
    })
    .catch(error => {
        console.log("Error (fetch): ", error);
    })
}

function updateCart(data)
{
    const cart = document.getElementById('cart');
    cart.innerHTML = '';

    for(let item of data)
    {
        cart.innerHTML += `<tr>
                                <td>${item.id}</td>
                                <td>${item.name}</td>
                                <td>${item.description}</td>
                                <td>${item.price} &euro;</td>
                                <td>${item.value}</td>
                                <td><a href="#" class="button" id=${'cartButtAdd:' + item.id} >
									<img id=${'cartAdd:' + item.id} onclick="addItem(event) src="./images/add-button.gif" alt="add""/></a></td>
                                <td><a href="#" class="button" id=${'cartButtRem:' + item.id}>
									<img id=${'cartRem:' + item.id} onclick="removeItem(event)" src="./images/rem-button.gif" alt="add"/></a></td>
                            </tr> `                
    }
}

function addItem(event)
{
    fetch('Shop?act=add&what=' + event.target.id.split(':')[1], { method: 'GET'})
    .then(response =>{
        console.log(response);
        return response.text();
    })
    .then(rawText => {
        return JSON.parse(rawText);
    })
    .then(data => {
        if(data != 'empty' && data != 'error')
        {
            this.updateCart(data.data);
        }
    })
    .catch(error => {
        console.log("Error (fetch): ", error);
    })
}

function removeItem(event)
{
    fetch('Shop?act=remove&what=' + event.target.id.split(':')[1], { method: 'GET'})
    .then(response =>{
        console.log(response);
        return response.text();
    })
    .then(rawText => {
        return JSON.parse(rawText);
    })
    .then(data => {
        if(data != 'empty' && data != 'error')
        {
            this.updateCart(data.data);
        }
    })
    .catch(error => {
        console.log("Error (fetch): ", error);
    })
}

function loadItems()
{
    fetch('Shop?act=get&what=items', { method: 'GET'})
    .then(response =>{
        console.log(response);
        return response.text();
    })
    .then(rawText => {
        return JSON.parse(rawText);
    })
    .then(data => {
        if(data != 'empty' && data != 'error')
        {
            this.updateItems(data.data);
        }
    })
    .catch(error => {
        console.log("Error (fetch): ", error);
    })
}

function updateItems(data)
{
    const cart = document.getElementById('items');
    cart.innerHTML = '';

    for(let item of data)
    {
        cart.innerHTML += `<tr>
                                <td>${item.id}</td>
                                <td>${item.name}</td>
                                <td>${item.description}</td>
                                <td>${item.price} &euro;</td>
                                <td>${item.value}</td>
                                <td><a href="#" class="button" id=${'itemsButtAdd:' + item.id}>
									<img id=${'itemsAdd:' + item.id} src="./images/add-button.gif" alt="add" onclick="addItem(event)"/></a></td>
                                <td><a href="#" class="button" id=${'itemsButtRem:' + item.id}>
									<img id=${'itemsRem:' + item.id} src="./images/rem-button.gif" alt="add" onclick="removeItem(event)"/></a></td>
                            </tr> `                
    }
}