// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract whiteList {
    mapping(address => bool) wallets;
    
    function insertList(address _wallet) external {
        wallets[_wallet] = true;
    }

    function removeList(address _wallet) external {
        wallets[_wallet] = false;
    }

    function checkWhiteList(address _wallet) external view returns(bool) {
        return wallets[_wallet];
    }
}