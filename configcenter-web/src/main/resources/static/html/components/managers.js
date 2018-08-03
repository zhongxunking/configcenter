const managerComponentTemplate = `
<div>
    <el-row>
        <el-col>
            <el-form :v-model="queryManagersForm" :inline="true" size="small">
                <el-form-item>
                    <el-input v-model="queryManagersForm.managerId" clearable placeholder="管理员id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="queryManagersForm.name" clearable placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="queryManagersForm.type" clearable placeholder="类型">
                        <el-option value="NORMAL" label="普通管理员"></el-option>
                        <el-option value="ADMIN" label="超级管理员"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="queryManagers">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-plus" @click="addManagerDialogVisible = true">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
    <el-table :data="managers" v-loading="managersLoading" border stripe>
        <el-table-column prop="managerId" label="管理员id">
            <template slot-scope="{ row }">
                <router-link :to="'/relations?managerId=' + row.managerId">
                    <el-button type="text">{{ row.managerId }}</el-button>
                </router-link>
            </template>
        </el-table-column>
        <el-table-column prop="name" label="名称">
            <template slot-scope="{ row }">
                <span v-if="!row.editing">{{ row.name }}</span>
                <el-input v-else v-model="row.editingName" size="small" clearable placeholder="请输入名称"></el-input>
            </template>
        </el-table-column>
        <el-table-column prop="type" label="类型">
            <template slot-scope="{ row }">
                <span v-if="!row.editing">
                    <el-tag v-if="row.type === 'NORMAL'" type="success">普通管理员</el-tag>
                    <el-tag v-else-if="row.type === 'ADMIN'" type="warning">超级管理员</el-tag>
                </span>
                <el-select v-else v-model="row.editingType" size="small" placeholder="请选择类型">
                    <el-option value="NORMAL" label="普通管理员"></el-option>
                    <el-option value="ADMIN" label="超级管理员"></el-option>
                </el-select>
            </template>
        </el-table-column>
        <el-table-column label="操作" header-align="center" width="160px">
            <template slot-scope="{ row }">
                <el-row>
                    <el-col :span="12" style="text-align: center">
                        <el-tooltip v-if="!row.editing" content="修改" placement="top" :open-delay="1000" :hide-after="3000">
                            <el-button @click="startEditing(row)" type="primary" icon="el-icon-edit" size="small" circle></el-button>
                        </el-tooltip>
                        <template v-else>
                            <el-button-group>
                                <el-tooltip content="取消修改" placement="top" :open-delay="1000" :hide-after="3000">
                                    <el-button @click="row.editing = false" type="info" icon="el-icon-close" size="small" circle></el-button>
                                </el-tooltip>
                                <el-popover placement="top" v-model="row.savePopoverShowing">
                                    <p>确定保存修改？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button size="mini" type="text" @click="row.savePopoverShowing = false">取消</el-button>
                                        <el-button type="primary" size="mini" @click="saveEditing(row)">确定</el-button>
                                    </div>
                                    <el-tooltip slot="reference" :disabled="row.savePopoverShowing" content="保存修改" placement="top" :open-delay="1000" :hide-after="3000">
                                        <el-button @click="row.savePopoverShowing = true" type="success" icon="el-icon-check" size="small" circle></el-button>
                                    </el-tooltip>
                                </el-popover>
                            </el-button-group>
                        </template>
                    </el-col>
                    <el-col :span="12" style="text-align: center">
                        <el-tooltip content="删除" placement="top" :open-delay="1000" :hide-after="3000">
                            <el-button @click="deleteManager(row)" type="danger" icon="el-icon-delete" size="small" circle></el-button>
                        </el-tooltip>
                    </el-col>
                </el-row>
            </template>
        </el-table-column>
    </el-table>
    <el-row style="margin-top: 10px">
        <el-col style="text-align: end">
            <el-pagination :total="totalManagers" :current-page.sync="queryManagersForm.pageNo" :page-size.sync="queryManagersForm.pageSize" @current-change="queryManagers" layout="total,prev,pager,next" small background></el-pagination>
        </el-col>
    </el-row>
    <el-dialog :visible.sync="addManagerDialogVisible" :before-close="closeAddManagerDialog" title="新增应用" width="40%">
        <el-form ref="addManagerForm" :model="addManagerForm" label-width="30%">
            <el-form-item label="管理员id" prop="managerId" :rules="[{required:true, message:'请输入管理员id', trigger:'blur'}]">
                <el-input v-model="addManagerForm.managerId" clearable placeholder="请输入管理员id" style="width: 90%"></el-input>
            </el-form-item>
            <el-form-item label="名称" prop="name" :rules="[{required:true, message:'请输入名称', trigger:'blur'}]">
                <el-input v-model="addManagerForm.name" clearable placeholder="请输入名称" style="width: 90%"></el-input>
            </el-form-item>
            <el-form-item label="类型" prop="type" :rules="[{required:true, message:'请选择类型', trigger:'blur'}]">
                <el-select v-model="addManagerForm.type" clearable placeholder="请选择类型" style="width: 90%">
                    <el-option value="NORMAL" label="普通管理员"></el-option>
                    <el-option value="ADMIN" label="超级管理员"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="初始密码" prop="password" :rules="[{required:true, message:'请输入初始密码', trigger:'blur'}]">
                <el-input v-model="addManagerForm.password" type="password" clearable placeholder="请输入初始密码" style="width: 90%"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="closeAddManagerDialog">取消</el-button>
            <el-button type="primary" @click="addManager">提交</el-button>
        </div>
    </el-dialog>
</div>
`;

const managersComponent = {
    template: managerComponentTemplate,
    data: function () {
        return {
            queryManagersForm: {
                pageNo: 1,
                pageSize: 10,
                managerId: null,
                type: null,
                name: null
            },
            managersLoading: false,
            totalManagers: 0,
            managers: [],
            addManagerDialogVisible: false,
            addManagerForm: {
                managerId: null,
                type: null,
                name: null,
                password: null
            }
        };
    },
    created: function () {
        this.queryManagers();
    },
    methods: {
        queryManagers: function () {
            this.managersLoading = true;
            const theThis = this;
            axios.get('../manager/manage/query', {params: this.queryManagersForm})
                .then(function (result) {
                    theThis.managersLoading = false;
                    if (!result.success) {
                        Vue.prototype.$message.error(result.message);
                        return;
                    }
                    theThis.totalManagers = result.totalCount;
                    theThis.managers = result.infos;
                    theThis.managers.forEach(function (manager) {
                        Vue.set(manager, 'editing', false);
                        Vue.set(manager, 'editingType', null);
                        Vue.set(manager, 'editingName', null);
                        Vue.set(manager, 'savePopoverShowing', false);
                    });
                });
        },
        startEditing: function (manager) {
            manager.editing = true;
            manager.editingType = manager.type;
            manager.editingName = manager.name;
        },
        saveEditing: function (manager) {
            manager.savePopoverShowing = false;

            const theThis = this;
            axios.post('../manager/manage/modifyType', {
                managerId: manager.managerId,
                newType: manager.editingType
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                axios.post('../manager/manage/modifyName', {
                    managerId: manager.managerId,
                    newName: manager.editingName
                }).then(function (result) {
                    if (result.success) {
                        Vue.prototype.$message.success(result.message);
                    } else {
                        Vue.prototype.$message.error(result.message);
                    }
                    theThis.queryManagers();
                });
            });
        },
        deleteManager: function (manager) {
            const theThis = this;
            Vue.prototype.$confirm('确定删除管理员？', '警告', {type: 'warning'})
                .then(function () {
                    axios.post('../manager/manage/delete', {managerId: manager.managerId})
                        .then(function (result) {
                            if (!result.success) {
                                Vue.prototype.$message.error(result.message);
                                return;
                            }
                            Vue.prototype.$message.success(result.message);
                            theThis.queryManagers();
                        });
                });
        },
        addManager: function () {
            const theThis = this;
            this.$refs.addManagerForm.validate(function (valid) {
                if (!valid) {
                    return;
                }
                axios.post('../manager/manage/add', theThis.addManagerForm)
                    .then(function (result) {
                        if (!result.success) {
                            Vue.prototype.$message.error(result.message);
                            return;
                        }
                        Vue.prototype.$message.success(result.message);
                        theThis.closeAddManagerDialog();
                        theThis.queryManagers();
                    });
            })
        },
        closeAddManagerDialog: function () {
            this.addManagerDialogVisible = false;
            this.addManagerForm.managerId = null;
            this.addManagerForm.type = null;
            this.addManagerForm.name = null;
            this.addManagerForm.password = null;
        }
    }
};