const TokenManager = artifacts.require("TokenManager");
const Whitelist = artifacts.require("whiteList");

module.exports = async function (deployer) {
    await deployer.deploy(Whitelist);
    const ManagerInstance = await TokenManager.deployed();
    const WhitelistInstance = await Whitelist.deployed();
    await ManagerInstance.setWhiteList(WhitelistInstance.address);
}