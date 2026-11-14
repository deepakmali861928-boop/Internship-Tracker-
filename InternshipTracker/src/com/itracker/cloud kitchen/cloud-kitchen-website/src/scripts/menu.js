const menuContainer = document.getElementById('menu-container');

async function fetchMenu() {
    try {
        const response = await fetch('../data/menu.json');
        const menuData = await response.json();
        renderMenu(menuData);
    } catch (error) {
        console.error('Error fetching menu:', error);
    }
}

function renderMenu(menuItems) {
    menuContainer.innerHTML = '';
    menuItems.forEach(item => {
        const menuCard = createMenuCard(item);
        menuContainer.appendChild(menuCard);
    });
}

function createMenuCard(item) {
    const card = document.createElement('div');
    card.className = 'menu-card';
    
    const title = document.createElement('h3');
    title.textContent = item.name;
    
    const description = document.createElement('p');
    description.textContent = item.description;
    
    const price = document.createElement('span');
    price.textContent = `$${item.price.toFixed(2)}`;
    
    const addButton = document.createElement('button');
    addButton.textContent = 'Add to Cart';
    addButton.onclick = () => addToCart(item);
    
    card.appendChild(title);
    card.appendChild(description);
    card.appendChild(price);
    card.appendChild(addButton);
    
    return card;
}

function addToCart(item) {
    // Logic to add item to cart
    console.log(`${item.name} added to cart`);
}

document.addEventListener('DOMContentLoaded', fetchMenu);