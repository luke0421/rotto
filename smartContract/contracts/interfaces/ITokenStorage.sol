// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "../MyStructs.sol";

interface ITokenStorage {
    function mint(uint code, uint amount) external;

    function burn(uint code, address _wallet) external;

    function transfer(Subscription memory subscription, address _to, uint amount) external;
}