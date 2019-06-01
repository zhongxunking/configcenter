// 操作权限管理组件
const OperatePrivilegesTemplate = `
<div>
    <div v-for="appOperatePrivilege in appOperatePrivileges" style="margin-bottom: 30px">
        <el-row v-if="appOperatePrivilege.app.appId === appId" style="margin-bottom: 10px">
            <el-col :offset="4" :span="16" style="text-align: center;">
                <span style="font-size: x-large;color: #409EFF;">{{ toShowingApp(appOperatePrivilege.app) }}</span>
            </el-col>
            <el-col :span="4" style="text-align: end;">
                <el-button type="primary" icon="el-icon-plus" @click="addOperatePrivilegeVisible = true" :disabled="manager.type !== 'ADMIN'" size="small">新增</el-button>
            </el-col>
        </el-row>
        <el-row v-else style="margin-bottom: 10px">
            <el-col :offset="4" :span="16" style="text-align: center">
                <span style="font-size: large;color: #67c23a;">{{ toShowingApp(appOperatePrivilege.app) }}</span>
            </el-col>
        </el-row>
        <el-table :data="appOperatePrivilege.showingKeyRegexPrivileges"
                  v-loading="loading"
                  :key="appOperatePrivilege.app.appId"
                  :default-sort="{prop: 'keyRegex'}"
                  :style="{width: appOperatePrivilege.app.appId === appId ? '100%' : 'calc(100% - 130px)'}"
                  :cell-style="{padding: '3px 0px'}"
                  border stripe>
            <el-table-column prop="keyRegex" label="配置key正则表达式" sortable>
                <template slot-scope="{ row }">
                    <span class="keyRegex-text-style">{{ row.keyRegex }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="privilege" label="操作权限" sortable>
                <template slot-scope="{ row }">
                    <div v-if="!row.editing">
                        <el-tag v-if="row.privilege === 'READ_WRITE'" type="success" size="medium">读写</el-tag>
                        <el-tag v-else-if="row.privilege === 'READ'" type="warning" size="medium">只读</el-tag>
                        <el-tag v-else-if="row.privilege === 'NONE'" type="danger" size="medium">无</el-tag>
                    </div>
                    <el-select v-else v-model="row.editingPrivilege" size="mini" placeholder="请选择操作权限" style="width: 90%">
                        <el-option value="READ_WRITE" label="读写"></el-option>
                        <el-option value="READ" label="只读"></el-option>
                        <el-option value="NONE" label="无"></el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column v-if="appOperatePrivilege.app.appId === appId" label="操作" header-align="center" width="130px">
                <template slot-scope="{ row }">
                    <el-row>
                        <el-col :span="16" style="text-align: center">
                            <el-tooltip v-if="!row.editing" content="修改" placement="top" :open-delay="1000" :hide-after="3000">
                                <el-button @click="startEditing(row)" type="primary" icon="el-icon-edit" :disabled="manager.type !== 'ADMIN'" size="mini" circle></el-button>
                            </el-tooltip>
                            <template v-else>
                                <el-button-group>
                                    <el-tooltip content="取消修改" placement="top" :open-delay="1000" :hide-after="3000">
                                        <el-button @click="row.editing = false" type="info" icon="el-icon-close" size="mini" circle></el-button>
                                    </el-tooltip>
                                    <el-tooltip content="保存修改" placement="top" :open-delay="1000" :hide-after="3000">
                                        <el-button @click="saveEditing(row)" type="success" icon="el-icon-check" size="mini" circle></el-button>
                                    </el-tooltip>
                                </el-button-group>
                            </template>
                        </el-col>
                        <el-col :span="8" style="text-align: center">
                            <el-tooltip content="删除" placement="top" :open-delay="1000" :hide-after="3000">
                                <el-button @click="deleteOperatePrivileges(row)" type="danger" icon="el-icon-delete" :disabled="manager.type !== 'ADMIN'" size="mini" circle></el-button>
                            </el-tooltip>
                        </el-col>
                    </el-row>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <el-dialog :visible.sync="addOperatePrivilegeVisible" :before-close="closeAddOperatePrivilegeDialog" title="新增操作权限" width="60%">
        <el-form ref="addOperatePrivilegeForm" :model="addOperatePrivilegeForm" label-width="30%">
            <el-form-item label="配置key正则表达式" prop="keyRegex" :rules="[{required:true, message:'请输入配置key的正则表达式', trigger:'blur'}]">
                <el-input v-model="addOperatePrivilegeForm.keyRegex" clearable placeholder="请输入配置key的正则表达式" style="width: 90%"></el-input>
            </el-form-item>
            <el-form-item label="操作权限" prop="privilege" :rules="[{required:true, message:'请选择操作权限', trigger:'blur'}]">
                <el-select v-model="addOperatePrivilegeForm.privilege" placeholder="请选择操作权限" style="width: 90%">
                    <el-option value="READ_WRITE" label="读写"></el-option>
                    <el-option value="READ" label="只读"></el-option>
                    <el-option value="NONE" label="无"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="closeAddOperatePrivilegeDialog">取消</el-button>
            <el-button type="primary" @click="addOperatePrivilege">提交</el-button>
        </div>
    </el-dialog>
</div>
`;

const OperatePrivileges = {
    template: OperatePrivilegesTemplate,
    props: ['appId'],
    data: function () {
        return {
            manager: CURRENT_MANAGER,
            loading: false,
            appOperatePrivileges: [],
            addOperatePrivilegeVisible: false,
            addOperatePrivilegeForm: {
                keyRegex: null,
                privilege: null
            }
        };
    },
    created: function () {
        this.findInheritedOperatePrivileges();
    },
    methods: {
        findInheritedOperatePrivileges: function () {
            this.loading = true;
            const theThis = this;
            axios.get('../manage/operatePrivilege/findInheritedOperatePrivileges', {
                params: {
                    appId: this.appId
                }
            }).then(function (result) {
                theThis.loading = false;
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                result.appOperatePrivileges.forEach(function (appOperatePrivilege) {
                    const showingKeyRegexPrivileges = [];
                    for (let keyRegex in appOperatePrivilege.keyRegexPrivileges) {
                        let privilege = appOperatePrivilege.keyRegexPrivileges[keyRegex];
                        showingKeyRegexPrivileges.push({
                            keyRegex: keyRegex,
                            privilege: privilege,
                            editing: false,
                            editingPrivilege: null
                        });
                    }
                    appOperatePrivilege.showingKeyRegexPrivileges = showingKeyRegexPrivileges;
                });
                theThis.appOperatePrivileges = result.appOperatePrivileges;
            });
        },
        startEditing: function (showingKeyRegexPrivilege) {
            showingKeyRegexPrivilege.editing = true;
            showingKeyRegexPrivilege.editingPrivilege = showingKeyRegexPrivilege.privilege;
        },
        saveEditing: function (showingKeyRegexPrivilege) {
            this.addOrModifyOperatePrivilege(this.appId, showingKeyRegexPrivilege.keyRegex, showingKeyRegexPrivilege.editingPrivilege);
        },
        addOperatePrivilege: function () {
            const theThis = this;
            this.$refs.addOperatePrivilegeForm.validate(function (valid) {
                if (!valid) {
                    return;
                }
                theThis.addOrModifyOperatePrivilege(theThis.appId, theThis.addOperatePrivilegeForm.keyRegex, theThis.addOperatePrivilegeForm.privilege);
            });
        },
        addOrModifyOperatePrivilege: function (appId, keyRegex, privilege) {
            const theThis = this;
            axios.post('../manage/operatePrivilege/addOrModifyOperatePrivilege', {
                appId: appId,
                keyRegex: keyRegex,
                privilege: privilege
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                Vue.prototype.$message.success(result.message);
                theThis.closeAddOperatePrivilegeDialog();
                theThis.findInheritedOperatePrivileges();
            });
        },
        deleteOperatePrivileges: function (showingKeyRegexPrivilege) {
            const theThis = this;
            Vue.prototype.$confirm('确定删除？', '警告', {type: 'warning'})
                .then(function () {
                    axios.post('../manage/operatePrivilege/deleteOperatePrivileges', {
                        appId: theThis.appId,
                        keyRegex: showingKeyRegexPrivilege.keyRegex
                    }).then(function (result) {
                        if (!result.success) {
                            Vue.prototype.$message.error(result.message);
                            return;
                        }
                        Vue.prototype.$message.success(result.message);
                        theThis.findInheritedOperatePrivileges();
                    });
                });
        },
        closeAddOperatePrivilegeDialog: function () {
            this.addOperatePrivilegeVisible = false;
            this.addOperatePrivilegeForm.keyRegex = null;
            this.addOperatePrivilegeForm.privilege = null;
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