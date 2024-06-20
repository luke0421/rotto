// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "./MyStructs.sol";
import "./interfaces/IWhitelist.sol";
import "./interfaces/ITokenStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract TokenManager is Ownable {
    address private tokenStorageAddress;
    address private whitelistAddress;

    modifier validAddress(address _addr) {
        require(_addr != address(0), unicode"올바르지 않은 주소입니다.");
        _;
    }

    modifier checklist(address _wallet) {
        bool check = IWhitelist(whitelistAddress).checkWhiteList(_wallet);
        require(check, unicode"허용되지 않는 지갑입니다.");
        _;
    }

    event resultCheck(bool);

    function setStorageAddress(address _addr) public validAddress(_addr) onlyOwner {
        tokenStorageAddress = _addr;
    }

    function setWhiteList(address _addr) public validAddress(_addr){
        whitelistAddress = _addr;
    }

    // 토큰 생성
    function createToken(Subscription memory subscription, uint amount) external onlyOwner {
        ITokenStorage(tokenStorageAddress).mint(subscription.code, amount);
    }

    // 토큰 발급
    function distributeToken(Subscription memory subscription, address _wallet, uint amount) external checklist(_wallet) validAddress(_wallet) onlyOwner{
        ITokenStorage(tokenStorageAddress).transfer(subscription, _wallet, amount);
    }

    // 토큰 환급(삭제)
    function deleteToken(uint code, address _wallet) external checklist(_wallet) validAddress(_wallet) onlyOwner {
        ITokenStorage(tokenStorageAddress).burn(code, _wallet);
    }

    // 입력받은 지갑 주소를 whitelist에 추가
    function insertList(address _wallet) external validAddress(_wallet) onlyOwner {
        IWhitelist(whitelistAddress).insertList(_wallet);
    }

    // 입력받은 지갑 주소를 whitelist에 제거
    function removeList(address _wallet) external checklist(_wallet) validAddress(_wallet) onlyOwner {
        IWhitelist(whitelistAddress).removeList(_wallet);
    }

    // 입력받은 지갑 주소가 whitelist에 존재 여부 확인
    function checkWhiteList(address _wallet) external validAddress(_wallet) onlyOwner returns(bool) {
        bool result = IWhitelist(whitelistAddress).checkWhiteList(_wallet);
        emit resultCheck(result);
        return result;
    } 


    // 컨트랙트 소유권 관련 function 접근 block
    function transferOwnership(address newOwner) public pure override {
        revert(unicode"잘못된 접근입니다.");
    }

    function renounceOwnership() public pure override {
        revert(unicode"잘못된 접근입니다.");
    }
}