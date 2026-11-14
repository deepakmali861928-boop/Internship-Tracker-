import React from 'react';

const MenuCard = ({ item, onAddToCart }) => {
    return (
        <div className="menu-card">
            <img src={item.image} alt={item.name} className="menu-card-image" />
            <h3 className="menu-card-title">{item.name}</h3>
            <p className="menu-card-description">{item.description}</p>
            <p className="menu-card-price">${item.price.toFixed(2)}</p>
            <button className="add-to-cart-button" onClick={() => onAddToCart(item)}>
                Add to Cart
            </button>
        </div>
    );
};

export default MenuCard;