export function createButton(label, onClick, className = '') {
    const button = document.createElement('button');
    button.innerText = label;
    button.className = className;
    button.onclick = onClick;
    return button;
}

export function createAddToCartButton(onClick) {
    return createButton('Add to Cart', onClick, 'add-to-cart-button');
}

export function createCheckoutButton(onClick) {
    return createButton('Checkout', onClick, 'checkout-button');
}