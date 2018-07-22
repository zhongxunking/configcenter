// 环境管理组件
const profilesComponent = {
    template: null,
    data: function () {
        return {
            queryProfilesForm: {
                pageNo: 1,
                pageSize: 20,
                profileId: null
            },
            profilesLoading: false,
            totalProfiles: 0,
            profiles: [],
            addProfileDialogVisible: false,
            addProfileForm: {
                profileId: null,
                memo: null
            }
        };
    },
    created: function () {
        this.queryProfiles();
    },
    methods: {
        queryProfiles: function () {
            this.profilesLoading = true;

            const theThis = this;
            this.doQueryProfiles(this.queryProfilesForm, function (result) {
                theThis.totalProfiles = result.totalCount;
                theThis.profiles = result.infos;
                theThis.profiles.forEach(function (profile) {
                    Vue.set(profile, 'editing', false);
                    Vue.set(profile, 'editingMemo', null);
                    Vue.set(profile, 'savePopoverShowing', false);
                });
                theThis.profilesLoading = false;
            }, function () {
                theThis.profilesLoading = false;
            });
        },
        startEditing: function (profile) {
            profile.editing = true;
            profile.editingMemo = profile.memo;
        },
        saveEditing: function (profile) {
            profile.savePopoverShowing = false;
            this.doAddOrModifyProfile({
                profileId: profile.profileId,
                memo: profile.editingMemo
            }, function () {
                profile.editing = false;
                profile.memo = profile.editingMemo;
            });
        },
        deleteProfile: function (profile) {
            const theThis = this;
            Vue.prototype.$confirm('确定删除环境？', '警告', {type: 'warning'})
                .then(function () {
                    axios.post('../manage/profile/deleteProfile', {profileId: profile.profileId})
                        .then(function (result) {
                            if (!result.success) {
                                Vue.prototype.$message.error(result.message);
                                return;
                            }
                            Vue.prototype.$message.success(result.message);
                            theThis.queryProfiles();
                        });
                });
        },
        addProfile: function () {
            const theThis = this;
            this.$refs.addProfileForm.validate(function (valid) {
                if (!valid) {
                    return;
                }
                theThis.doAddOrModifyProfile(theThis.addProfileForm, function () {
                    theThis.closeAddProfileDialog();
                    theThis.queryProfiles();
                });
            })
        },
        closeAddProfileDialog: function () {
            this.addProfileDialogVisible = false;
            this.addProfileForm.profileId = null;
            this.addProfileForm.memo = null;
        },
        doQueryProfiles: function (params, processResult, failCallback) {
            axios.get('../manage/profile/queryProfiles', {params: params})
                .then(function (result) {
                    if (result.success) {
                        processResult(result);
                    } else {
                        Vue.prototype.$message.error(result.message);
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                });
        },
        doAddOrModifyProfile: function (params, successCallback) {
            axios.post('../manage/profile/addOrModifyProfile', params)
                .then(function (result) {
                    if (!result.success) {
                        Vue.prototype.$message.error(result.message);
                        return;
                    }
                    Vue.prototype.$message.success(result.message);
                    successCallback();
                });
        }
    }
};
// 获取组件页面
axios.get('./components/profiles.html').then(function (result) {
    profilesComponent.template = result;
});
