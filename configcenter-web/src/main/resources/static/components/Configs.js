// 配置查看组件
const ConfigsTemplate = `
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
        <el-table-column prop="appId" label="应用" width="400px">
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
                <router-link v-for="(profile, index) in allProfiles" v-if="index < 12" :to="'/configs/' + row.appId +'/' + profile.profileId" :key="profile.profileId" style="margin-right: 10px">
                    <el-button type="text">{{ profile.profileId }}</el-button>
                </router-link>
                <router-link v-if="allProfiles.length > 12" :to="'/configs/' + row.appId +'/' + allProfiles[0].profileId" style="margin-right: 10px">
                    <el-button type="text" icon="el-icon-more"></el-button>
                </router-link>
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

const Configs = {
    template: ConfigsTemplate,
    data: function () {
        return {
            queryAppsForm: {
                pageNo: 1,
                pageSize: 10,
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
            axios.get('../manage/app/queryManagedApps', {params: this.queryAppsForm})
                .then(function (result) {
                    theThis.appsLoading = false;
                    if (!result.success) {
                        Vue.prototype.$message.error(result.message);
                        return;
                    }
                    theThis.totalApps = result.totalCount;
                    theThis.apps = result.infos;
                });

            this.allProfilesLoading = true;
            axios.get('../manage/profile/findProfileTree', {
                params: {
                    profileId: null
                }
            }).then(function (result) {
                theThis.allProfilesLoading = false;
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                let extractProfiles = function (profileTree, level) {
                    let profiles = [];
                    if (profileTree.profile !== null) {
                        profileTree.profile.level = level;
                        profiles.push(profileTree.profile);
                    }
                    profileTree.children.forEach(function (child) {
                        profiles = profiles.concat(extractProfiles(child, level + 1));
                    });
                    return profiles;
                };
                theThis.allProfiles = extractProfiles(result.profileTree, -1);
            });
        },
        toShowingApp: function (app) {
            if (!app) {
                return '';
            }
            let text = app.appId;
            if (app.appName) {
                text += '（' + app.appName + '）';
            }
            return text;
        }
    }
};