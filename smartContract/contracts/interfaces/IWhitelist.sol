// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

interface IWhitelist {
    function insertList(address _wallet) external;

    function removeList(address _wallet) external;

    function checkWhiteList(address _wallet) external view returns(bool);
}