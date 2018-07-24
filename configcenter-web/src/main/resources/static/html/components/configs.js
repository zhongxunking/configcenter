const configsComponentTemplate = `
<div>
    <el-row>
        <el-col>
            <el-form :v-model="queryAppsForm" :inline="true" size="small">
                <el-form-item>
                    <el-input v-model="queryAppsForm.appId" clearable placeholder="应用id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="queryConfigs">查询</el-button>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
    <el-table :data="apps" v-loading="appsLoading && allProfilesLoading" border stripe>
        <el-table-column label="应用">
            <template slot-scope="{ row }">
                <router-link :to="'/configs/' + row.appId">
                    <el-button type="text">
                        {{ toShowingApp(row) }}
                    </el-button>
                </router-link>
            </template>
        </el-table-column>
        <el-table-column label="环境">
            <template slot-scope="{ row }">
                <el-button v-for="profile in allProfiles" type="text" :key="profile.profileId">{{ profile.profileId }}</el-button>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="160px">
            <template slot-scope="{ row }">
                <el-button type="text">应用树</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-row style="margin-top: 10px">
        <el-col style="text-align: end">
            <el-pagination :total="totalApps" :current-page.sync="queryAppsForm.pageNo" :page-size.sync="queryAppsForm.pageSize" @current-change="queryConfigs" layout="total,prev,pager,next" small background></el-pagination>
        </el-col>
    </el-row>
</div>
`;

const configsComponent = {
    template: configsComponentTemplate,
    data: function () {
        return {
            queryAppsForm: {
                pageNo: 1,
                pageSize: 20,
                appId: null
            },
            appsLoading: false,
            totalApps: 0,
            apps: [],
            allProfilesLoading: false,
            allProfiles: []
        };
    },
    created: function () {
        this.queryConfigs();
    },
    methods: {
        queryConfigs: function () {
            const theThis = this;

            this.appsLoading = true;
            this.doQueryApps(this.queryAppsForm, function (result) {
                theThis.totalApps = result.totalCount;
                theThis.apps = result.infos;
                theThis.appsLoading = false;
            }, function () {
                theThis.appsLoading = false;
            });

            this.allProfilesLoading = true;
            this.doFindAllProfiles(function (result) {
                theThis.allProfiles = result.profiles;
                theThis.allProfilesLoading = false;
            }, function () {
                theThis.allProfilesLoading = false;
            })
        },
        toShowingApp: function (app) {
            if (!app) {
                return '';
            }
            let text = app.appId;
            if (app.memo) {
                text += '（' + app.memo + '）';
            }
            return text;
        },
        doQueryApps: function (params, processResult, failCallback) {
            axios.get('../manage/app/queryApps', {params: params})
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
        doFindAllProfiles: function (processResult, failCallback) {
            axios.get('../manage/profile/findAllProfiles')
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
        }
    }
};
