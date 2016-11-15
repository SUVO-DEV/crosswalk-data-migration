/*global cordova, module*/

module.exports = {
    migratedata: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "CrosswalkDataMigration", "migratedata", [name]);
    }
};
