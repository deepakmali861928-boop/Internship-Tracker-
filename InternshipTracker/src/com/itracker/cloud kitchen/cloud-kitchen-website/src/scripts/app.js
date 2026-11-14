// This is the main JavaScript file that initializes the application, sets up event listeners, and manages the overall functionality of the website.

import { renderMenu } from './menu.js';
import { setupCart } from './cart.js';
import { setupCheckout } from './checkout.js';
import { setupPayments } from './payments.js';

document.addEventListener('DOMContentLoaded', () => {
    renderMenu();
    setupCart();
    setupCheckout();
    setupPayments();
});