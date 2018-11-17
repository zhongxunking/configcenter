// 属性值管理组件
const PropertyValuesTemplate = `
<div>
    <el-row style="margin-bottom: 10px">
        <el-col>
            <span style="font-size: large;color: #409EFF;">环境：</span>
            <el-select v-model="currentProfileId" @change="switchProfile" placeholder="请选择环境" size="medium">
                <el-option v-for="profile in allProfiles" :value="profile.profileId" :label="toShowingProfile(profile)" :key="profile.profileId">
                    <span v-for="i in profile.level">-</span>
                    <span>{{ toShowingProfile(profile) }}</span>
                </el-option>
            </el-select>
        </el-col>
    </el-row>
    <div v-for="appProperty in appProperties" style="margin-bottom: 50px">
        <el-row v-if="appProperty.appId === appId" style="margin-bottom: 10px">
            <el-col :offset="6" :span="12" style="text-align: center;">
                <span style="font-size: x-large;color: #409EFF;">{{ toShowingApp(appProperty.app) }}</span>
            </el-col>
            <el-col :span="6" style="text-align: end">
                <el-button icon="el-icon-close" @click="findAppProperties" :disabled="!edited" size="small">取消修改</el-button>
                <el-popover placement="top" v-model="submitPopoverShowing">
                    <p>提交后应用的配置将被修改，确定提交？</p>
                    <div style="text-align: right; margin: 0">
                        <el-button size="mini" type="text" @click="submitPopoverShowing = false">取消</el-button>
                        <el-button type="primary" size="mini" @click="submitEdited">确定</el-button>
                    </div>
                    <el-button slot="reference" type="primary" icon="el-icon-upload" @click="submitPopoverShowing = true" :disabled="!edited" size="small">提交修改</el-button>
                </el-popover>
            </el-col>
        </el-row>
        <el-row v-else style="margin-bottom: 10px">
            <el-col :offset="4" :span="16" style="text-align: center;">
                <span style="font-size: large;color: #67c23a;">{{ toShowingApp(appProperty.app) }}</span>
            </el-col>
        </el-row>

        <el-table v-for="profileProperty in appProperty.profileProperties"
                  :data="profileProperty.properties.length !== 0 ? profileProperty.properties : [{empty:true, propertiesSize:1, appId:appProperty.appId, profileId:profileProperty.profileId}]"
                  :span-method="profilePropertySpanMethod"
                  :show-header="profileProperty.profileId === profileId"
                  :key="appProperty.appId + ':' + profileProperty.profileId"
                  :default-sort="{prop: 'key'}"
                  :style="{width: appProperty.appId === appId ? '100%' : 'calc(100% - 90px)'}"
                  border stripe>
            <el-table-column label="环境" :resizable="false" width="200px">
                <template slot-scope="{ row }">
                    <span>{{ toShowingProfile(profileProperty.profile) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="key" label="属性key" :resizable="false" :align="profileProperty.properties.length !== 0 ? 'left' : 'center'">
                <template slot-scope="{ row }">
                    <span v-if="row.empty" class="el-table__empty-text">暂无数据</span>
                    <span v-else>{{ row.key }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="value" label="属性值" :resizable="false">
                <template slot-scope="{ row }">
                    <div v-if="!row.editing">
                        <div v-if="row.editedValue === row.value">
                            <el-tag v-if="row.editedValue === null">无效</el-tag>
                            <el-tag v-else-if="manager.type === 'NORMAL' && row.privilege === 'NONE'" type="danger">无权限</el-tag>
                            <span v-else>{{ row.editedValue }}</span>
                        </div>
                        <div v-else style="margin-top: 10px; margin-right: 30px">
                            <el-badge value="已修改">
                                <el-tag v-if="row.editedValue === null">无效</el-tag>
                                <span v-else>{{ row.editedValue }}</span>
                            </el-badge>
                        </div>
                    </div>
                    <el-input v-else v-model="row.editingValue" type="textarea" autosize size="small" clearable placeholder="请输入属性值"></el-input>
                </template>
            </el-table-column>
            <el-table-column prop="scope" label="作用域" :resizable="false" width="100px">
                <template slot-scope="{ row }">
                    <div>
                        <el-tag v-if="row.scope === 'PRIVATE'">私有</el-tag>
                        <el-tag v-else-if="row.scope === 'PROTECTED'" type="success">可继承</el-tag>
                        <el-tag v-else-if="row.scope === 'PUBLIC'" type="warning">公开</el-tag>
                    </div>
                </template>
            </el-table-column>
            <el-table-column v-if="appProperty.appId === appId" label="操作" header-align="center" align="center" :resizable="false" width="90px">
                <template slot-scope="{ row }">
                    <template v-if="profileProperty.profileId === profileId">
                        <el-tooltip v-if="!row.editing" content="修改" placement="top" :open-delay="1000" :hide-after="3000">
                            <el-button @click="startEditing(row)" type="primary" :disabled="manager.type === 'NORMAL' && row.privilege !== 'READ_WRITE'" icon="el-icon-edit" size="small" circle></el-button>
                        </el-tooltip>
                        <el-button-group v-else>
                            <el-tooltip content="取消修改" placement="top" :open-delay="1000" :hide-after="3000">
                                <el-button @click="cancelEditing(row)" type="info" icon="el-icon-close" size="small" circle></el-button>
                            </el-tooltip>
                            <el-tooltip content="确认修改" placement="top" :open-delay="1000" :hide-after="3000">
                                <el-button @click="saveEditing(row)" type="success" icon="el-icon-check" size="small" circle></el-button>
                            </el-tooltip>
                        </el-button-group>
                    </template>
                    <template v-else-if="showOverride(row.key)">
                        <el-tooltip content="覆盖" placement="top" :open-delay="1000" :hide-after="3000">
                            <el-button @click="overrideProperty(row)" :disabled="manager.type === 'NORMAL' && row.privilege !== 'READ_WRITE'" icon="el-icon-download" size="small" circle></el-button>
                        </el-tooltip>
                    </template>
                </template>
            </el-table-column>
        </el-table>
    </div>
</div>
`;

const PropertyValues = {
    template: PropertyValuesTemplate,
    props: ['appId', 'profileId'],
    data: function () {
        return {
            currentProfileId: this.profileId,
            manager: CURRENT_MANAGER,
            allProfiles: [],
            selfPropertiesLoading: false,
            appProperties: [],
            submitPopoverShowing: false
        };
    },
    computed: {
        edited: function () {
            const theThis = this;

            let edited = false;
            this.appProperties.forEach(function (appProperty) {
                if (appProperty.appId !== theThis.appId) {
                    return;
                }
                appProperty.profileProperties.forEach(function (profileProperty) {
                    if (profileProperty.profileId !== theThis.profileId) {
                        return;
                    }
                    profileProperty.properties.forEach(function (property) {
                        edited = edited || property.editedValue !== property.value;
                    });
                })
            });
            return edited;
        }
    },
    watch: {
        '$route': function () {
            this.findAllProfiles();
            this.findAppProperties();
        }
    },
    created: function () {
        this.findAllProfiles();
        this.findAppProperties();
    },
    methods: {
        findAllProfiles: function () {
            const theThis = this;
            axios.get('../manage/profile/findProfileTree', {
                params: {
                    profileId: null
                }
            }).then(function (result) {
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
        switchProfile: function (profileId) {
            this.$router.replace('/configs/' + this.appId + '/' + profileId);
        },
        findAppProperties: function () {
            const theThis = this;
            this.selfPropertiesLoading = true;
            axios.get('../manage/propertyValue/findInheritedProperties', {
                params: {
                    appId: this.appId,
                    profileId: this.profileId
                }
            }).then(function (result) {
                theThis.selfPropertiesLoading = false;
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                theThis.appProperties = result.appProperties;
                theThis.doFindPropertyKeys(theThis.appId, function (propertyKeys) {
                    let propertyKeysMap = {};
                    propertyKeys.forEach(function (propertyKey) {
                        propertyKeysMap[propertyKey.key] = propertyKey;
                    });
                    let newSize = 0;
                    theThis.appProperties.forEach(function (appProperty) {
                        if (appProperty.appId !== theThis.appId) {
                            return;
                        }
                        appProperty.profileProperties.forEach(function (profileProperty) {
                            if (profileProperty.profileId === theThis.profileId) {
                                newSize = profileProperty.properties.length;
                            }
                            profileProperty.properties.forEach(function (property) {
                                delete propertyKeysMap[property.key];
                            });
                        });
                    });
                    newSize += Object.getOwnPropertyNames(propertyKeysMap).length;

                    theThis.appProperties.forEach(function (appProperty) {
                        if (appProperty.appId !== theThis.appId) {
                            return;
                        }
                        appProperty.profileProperties.forEach(function (profileProperty) {
                            if (profileProperty.profileId !== theThis.profileId) {
                                return;
                            }
                            for (let key in propertyKeysMap) {
                                profileProperty.properties.push({
                                    propertiesSize: newSize,
                                    key: propertyKeysMap[key].key,
                                    value: null,
                                    scope: propertyKeysMap[key].scope,
                                    editing: false,
                                    editingValue: null,
                                    editedValue: null
                                });
                            }
                            profileProperty.properties.forEach(function (property) {
                                Vue.set(property, 'propertiesSize', newSize);
                            });
                        });
                    });
                });

                theThis.appProperties.forEach(function (appProperty) {
                    Vue.set(appProperty, 'app', null);
                    theThis.doFindApp(appProperty.appId, function (app) {
                        appProperty.app = app;
                    });
                    appProperty.profileProperties.forEach(function (profileProperty) {
                        Vue.set(profileProperty, 'profile', null);
                        theThis.doFindProfile(profileProperty.profileId, function (profile) {
                            profileProperty.profile = profile;
                        });
                        profileProperty.properties.forEach(function (property) {
                            Vue.set(property, 'propertiesSize', profileProperty.properties.length);
                            Vue.set(property, 'editing', false);
                            Vue.set(property, 'editingValue', null);
                            Vue.set(property, 'editedValue', property.value);
                            Vue.set(property, 'privilege', 'READ_WRITE');
                        });
                    });
                });

                theThis.appProperties.forEach(function (appProperty) {
                    axios.get('../manage/propertyKey/findKeyPrivileges', {
                        params: {
                            appId: appProperty.appId
                        }
                    }).then(function (result) {
                        if (!result.success) {
                            Vue.prototype.$message.error(result.message);
                            return;
                        }
                        const keyPrivileges = result.keyPrivileges;
                        appProperty.profileProperties.forEach(function (profileProperty) {
                            profileProperty.properties.forEach(function (property) {
                                let privilege = keyPrivileges[property.key];
                                if (!privilege) {
                                    privilege = 'READ_WRITE';
                                }
                                property.privilege = privilege;
                            });
                        });
                    });
                });
            });
        },
        doFindApp: function (appId, processApp) {
            axios.get('../manage/app/findApp', {
                params: {
                    appId: appId
                }
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                processApp(result.app);
            });
        },
        doFindProfile: function (profileId, processProfile) {
            axios.get('../manage/profile/findProfile', {
                params: {
                    profileId: profileId
                }
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                processProfile(result.profile);
            });
        },
        doFindPropertyKeys: function (appId, processor) {
            const theThis = this;

            axios.get('../manage/propertyKey/findInheritedPropertyKeys', {
                params: {
                    appId: appId
                }
            }).then(function (result) {
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                result.appPropertyKeys.forEach(function (appPropertyKey) {
                    if (appPropertyKey.appId !== theThis.appId) {
                        return;
                    }
                    processor(appPropertyKey.propertyKeys);
                });
            });
        },
        showOverride: function (key) {
            const theThis = this;

            let show = true;
            this.appProperties.forEach(function (appProperty) {
                if (appProperty.appId !== theThis.appId) {
                    return;
                }
                appProperty.profileProperties.forEach(function (profileProperty) {
                    if (profileProperty.profileId !== theThis.profileId) {
                        return;
                    }
                    profileProperty.properties.forEach(function (property) {
                        if (property.key === key) {
                            show = false;
                        }
                    });
                });
            });
            return show;
        },
        profilePropertySpanMethod: function ({row, column, rowIndex, columnIndex}) {
            if (columnIndex === 0) {
                if (rowIndex === 0) {
                    return [row.propertiesSize, 1];
                } else {
                    return [0, 0];
                }
            }
            if (row.empty) {
                if (columnIndex !== 1) {
                    return [0, 0];
                } else {
                    if (row.appId === this.appId) {
                        return [1, 4];
                    } else {
                        return [1, 3];
                    }
                }
            }
        },
        overrideProperty: function (property) {
            const theThis = this;

            this.appProperties.forEach(function (appProperty) {
                if (appProperty.appId !== theThis.appId) {
                    return;
                }
                appProperty.profileProperties.forEach(function (profileProperty) {
                    if (profileProperty.profileId !== theThis.profileId) {
                        return;
                    }
                    for (let temp in profileProperty.properties) {
                        if (temp.key === property.key) {
                            return;
                        }
                    }

                    let newSize = profileProperty.properties.length + 1;
                    let temp = {
                        propertiesSize: newSize,
                        key: property.key,
                        value: null,
                        scope: property.scope,
                        editing: false,
                        editingValue: null,
                        editedValue: null
                    };
                    profileProperty.properties.push(temp);
                    profileProperty.properties.forEach(function (property) {
                        Vue.set(property, 'propertiesSize', newSize);
                    });
                    theThis.startEditing(temp);
                });
            });
        },
        startEditing: function (property) {
            property.editing = true;
            property.editingValue = property.editedValue;
        },
        cancelEditing: function (property) {
            property.editing = false;
            property.editingValue = null;

            this.removeIfNecessary(property);
        },
        saveEditing: function (property) {
            property.editing = false;
            property.editedValue = property.editingValue;
            if (property.editedValue !== null) {
                property.editedValue = property.editedValue.trim();
            }
            if (!property.editedValue) {
                property.editedValue = null;
            }
            property.editingValue = null;

            if (this.removeIfNecessary(property)) {
                Vue.prototype.$message.error("无效的覆盖");
            }
        },
        removeIfNecessary: function (property) {
            if (property.value !== null || property.editedValue !== null) {
                return false;
            }

            const theThis = this;

            let remove = false;
            this.appProperties.forEach(function (appProperty) {
                if (appProperty.appId !== theThis.appId) {
                    return;
                }
                appProperty.profileProperties.forEach(function (profileProperty) {
                    if (remove || profileProperty.profileId === theThis.profileId) {
                        return;
                    }
                    profileProperty.properties.forEach(function (tempProperty) {
                        remove = remove || tempProperty.key === property.key;
                    });
                });
            });
            if (!remove) {
                return false;
            }
            this.appProperties.forEach(function (appProperty) {
                if (appProperty.appId !== theThis.appId) {
                    return;
                }
                appProperty.profileProperties.forEach(function (profileProperty) {
                    if (profileProperty.profileId !== theThis.profileId) {
                        return;
                    }
                    for (let i = 0; i < profileProperty.properties.length; i++) {
                        if (profileProperty.properties[i].key === property.key) {
                            profileProperty.properties.splice(i, 1);
                            break;
                        }
                    }
                });
            });
            return true;
        },
        submitEdited: function () {
            this.submitPopoverShowing = false;

            const theThis = this;
            let keys = [], values = [];
            this.appProperties.forEach(function (appProperty) {
                if (appProperty.appId !== theThis.appId) {
                    return;
                }
                appProperty.profileProperties.forEach(function (profileProperty) {
                    if (profileProperty.profileId !== theThis.profileId) {
                        return;
                    }
                    profileProperty.properties.forEach(function (property) {
                        if (property.editedValue !== property.value) {
                            keys.push(property.key);
                            values.push(property.editedValue);
                        }
                    })
                })
            });

            this.selfPropertiesLoading = true;
            axios.post('../manage/propertyValue/setPropertyValues', {
                appId: this.appId,
                profileId: this.profileId,
                keys: JSON.stringify(keys),
                values: JSON.stringify(values)
            }).then(function (result) {
                theThis.selfPropertiesLoading = false;
                if (!result.success) {
                    Vue.prototype.$message.error(result.message);
                    return;
                }
                Vue.prototype.$message.success(result.message);
                theThis.findAppProperties();
            });
        },
        toShowingProfile: function (profile) {
            if (!profile) {
                return '';
            }
            let text = profile.profileId;
            if (profile.profileName) {
                text += '（' + profile.profileName + '）';
            }
            return text;
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