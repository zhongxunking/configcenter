// 配置类型组件
const PropertyTypesTemplate = `
<div id="propertyTypesApp">
    <div v-for="appRule in appRules" style="margin-bottom: 30px">
        <el-row v-if="appRule.app.appId === appId" style="margin-bottom: 10px">
            <el-col :offset="4" :span="16" style="text-align: center;">
                <span style="font-size: x-large;color: #409EFF;">{{ toShowingApp(appRule.app) }}</span>
            </el-col>
            <el-col :span="4" style="text-align: end;">
                <el-button type="primary" icon="el-icon-plus" @click="addRuleVisible = true" :disabled="manager.type !== 'ADMIN'" size="small">新增</el-button>
            </el-col>
        </el-row>
        <el-row v-else style="margin-bottom: 10px">
            <el-col :offset="4" :span="16" style="text-align: center">
                <span style="font-size: large;color: #67c23a;">{{ toShowingApp(appRule.app) }}</span>
            </el-col>
        </el-row>
        <el-table :data="appRule.rules"
                  :key="appRule.app.appId"
                  :default-sort="{prop: 'priority'}"
                  :style="{width: appRule.app.appId === appId ? '100%' : 'calc(100% - 130px)'}"
                  :cell-style="{padding: '3px 0px'}"
                  border stripe>
            <el-table-column prop="keyRegex" label="配置key正则表达式">
                <template slot-scope="{ row }">
                    <span class="keyRegex-text-style">{{ row.keyRegex }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="propertyType" label="普通管理员权限">
                <template slot-scope="{ row }">
                    <div v-if="!row.editing">
                        <el-tag v-if="row.propertyType === 'READ_WRITE'" type="success" size="medium">读写</el-tag>
                        <el-tag v-else-if="row.propertyType === 'READ'" type="warning" size="medium">只读</el-tag>
                        <el-tag v-else-if="row.propertyType === 'NONE'" type="danger" size="medium">无</el-tag>
                    </div>
                    <el-select v-else v-model="row.editingPropertyType" size="mini" placeholder="请选择权限" style="width: 90%">
                        <el-option value="READ_WRITE" label="读写"></el-option>
                        <el-option value="READ" label="只读"></el-option>
                        <el-option value="NONE" label="无"></el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级（值越小优先级越高）">
                <template slot-scope="{ row }">
                    <span v-if="!row.editing">{{ row.priority }}</span>
                    <el-input-number v-else v-model="row.editingPriority" size="small" :min="0" controls-position="right"></el-input-number>
                </template>
            </el-table-column>
            <el-table-column v-if="appRule.app.appId === appId" label="操作" header-align="center" width="130px">
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
                                <el-button @click="deleteRule(row)" type="danger" icon="el-icon-delete" :disabled="manager.type !== 'ADMIN'" size="mini" circle></el-button>
                            </el-tooltip>
                        </el-col>
                    </el-row>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <el-dialog :visible.sync="addRuleVisible" :before-close="closeAddRuleDialog" title="新增权限" width="60%">
        <el-form ref="addRuleForm" :model="addRuleForm" label-width="30%">
            <el-form-item label="配置key正则表达式" prop="keyRegex" :rules="[{required:true, message:'请输入配置key的正则表达式', trigger:'blur'}]">
                <el-input v-model="addRuleForm.keyRegex" clearable placeholder="请输入配置key的正则表达式" style="width: 90%"></el-input>
            </el-form-item>
            <el-form-item label="权限" prop="propertyType" :rules="[{required:true, message:'请选择权限', trigger:'blur'}]">
                <el-select v-model="addRuleForm.propertyType" placeholder="请选择权限" style="width: 90%">
                    <el-option value="READ_WRITE" label="读写"></el-option>
                    <el-option value="READ" label="只读"></el-option>
                    <el-option value="NONE" label="无"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="优先级" prop="priority" :rules="[{required:true, message:'请输入优先级', trigger:'blur'}]">
                <el-input-number v-model="addRuleForm.priority" :min="0" controls-position="right" style="width: 90%"></el-input-number>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="closeAddRuleDialog">取消</el-button>
            <el-button type="primary" @click="addRule">提交</el-button>
        </div>
    </el-dialog>
</div>
`;

const PropertyTypes = {
    template: PropertyTypesTemplate,
    props: ['appId'],
    data: function () {
        return {
            manager: CURRENT_MANAGER,
            appRules: [],
            inheritedAppRules: [],
            addRuleVisible: false,
            addRuleForm: {
                keyRegex: null,
                propertyType: null,
                priority: 0
            }
        };
    },
    watch: {
        inheritedAppRules: function () {
            this.refreshAppRules();
        }
    },
    created: function () {
        this.findInheritedAppRules();
    },
    methods: {
        refreshAppRules: function () {
            let appRules = [];
            this.inheritedAppRules.forEach(function (inheritedAppRule) {
                let appRule = {
                    app: inheritedAppRule.app,
                    rules: []
                };
                inheritedAppRule.rules.forEach(function (rule) {
                    appRule.rules.push({
                        keyRegex: rule.keyRegex,
                        propertyType: rule.propertyType,
                        priority: rule.priority,
                        editing: false,
                        editingPropertyType: null,
                        editingPriority: null
                    });
                });
                appRules.push(appRule);
            });

            this.appRules = appRules;
        },
        findInheritedAppRules: function (callback) {
            const theThis = this;
            axios.get('../manage/propertyType/findInheritedAppRules', {
                params: {
                    appId: this.appId
                }
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                theThis.inheritedAppRules = result.inheritedAppRules;
                if (callback) {
                    callback(theThis.inheritedAppRules);
                }
            });
        },
        startEditing: function (rule) {
            rule.editing = true;
            rule.editingPropertyType = rule.propertyType;
            rule.editingPriority = rule.priority;
        },
        saveEditing: function (rule) {
            const theThis = this;
            this.doAddOrModifyRule(rule.keyRegex, rule.editingPropertyType, rule.editingPriority, function () {
                theThis.findInheritedAppRules();
            });
        },
        addRule: function () {
            const theThis = this;
            this.$refs.addRuleForm.validate(function (valid) {
                if (!valid) {
                    return;
                }
                theThis.doAddOrModifyRule(theThis.addRuleForm.keyRegex, theThis.addRuleForm.propertyType, theThis.addRuleForm.priority, function () {
                    theThis.closeAddRuleDialog();
                    theThis.findInheritedAppRules();
                });
            });
        },
        doAddOrModifyRule: function (keyRegex, propertyType, priority, callback) {
            const theThis = this;
            axios.post('../manage/propertyType/addOrModifyRule', {
                appId: this.appId,
                keyRegex: keyRegex,
                propertyType: propertyType,
                priority: priority
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                Vue.prototype.$message.success(result.message);
                if (callback) {
                    callback();
                }
            });
        },
        deleteRule: function (rule) {
            const theThis = this;
            Vue.prototype.$confirm('确定删除？', '警告', {type: 'warning'})
                .then(function () {
                    axios.post('../manage/propertyType/deleteRule', {
                        appId: theThis.appId,
                        keyRegex: rule.keyRegex
                    }).then(function (result) {
                        if (!result.success) {
                            Vue.prototype.$message.error(result.message);
                            return;
                        }
                        Vue.prototype.$message.success(result.message);
                        theThis.findInheritedAppRules();
                    });
                });
        },
        closeAddRuleDialog: function () {
            this.addRuleVisible = false;
            this.addRuleForm.keyRegex = null;
            this.addRuleForm.propertyType = null;
            this.addRuleForm.priority = 0;
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