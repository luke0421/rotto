const TokenManager = artifacts.require("TokenManager");

module.exports = async function (deployer) {
    deployer.deploy(TokenManager);
};