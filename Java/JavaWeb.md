# Java Web

`更新时间：2026-3-11`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## Vue

`Vue`是一款用于构建用户界面的渐进式`JavaScript`框架

构建用户界面指将后端得到的`json`数据渲染为用户能够看到的界面。渐进式是指`Vue`的设计可以逐步采用，使用分层的核心思想，可以从一个简单的功能开始，然后根据需要逐步引入更多 `Vue`的能力，而不需要一开始就做全盘的重构

### Vue快速入门

通过一个案例来进行`Vue`的快速入门，案例要求是使用`Vue`将一段来自后端的内容`message: "Hello, Vue"`渲染到前端，并进行加粗

- 准备

  - 从官方网站引入`Vue`模块

  ```html
  <script type="module">
  	import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
  </script>
  ```

  - 创建`Vue`程序的应用实例，控制视图的元素

  ```html
  <script type="module">
  	import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
  
  	createApp({
          
      })
  </script>
  ```

  - 准备元素`div`，并被`Vue`控制

  ```html
  <div class="app">
  	<!-- 被Vue控制区域 -->  
  </div>
  <script type="module">
  	import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
  
  	createApp({
          
      }).mount(".app")
  </script>
  ```

- 数据驱动识图

  - 准备数据，重写方法`data()`，返回需要的数据实例，数据类型为`object`，其中包含属性`message`。

  ```html
  <div class="app">
  	<!-- 被Vue控制区域 -->  
  </div>
  <script type="module">
  	import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
  
  	createApp({
          data() {
              return {
                  message: "Hello, Vue"
              }
          }
      }).mount(".app")
  </script>
  ```

  - 通过插值表达式`{{实例属性}}`渲染页面，注意插值表达式不能出现在标签内部

  ```html
  <div class="app">
  	<h1>{{message}}</h1> 
  </div>
  <script type="module">
  	import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
  
  	createApp({
          data() {
              return {
                  message: "Hello, Vue"
              }
          }
      }).mount(".app")
  </script>
  ```

> <img src="./javaweb/1.png">

### Vue常用指令

`Vue`常用指令是指`HTML`标签上带有`v-`前缀的特殊属性，不同的指令拥有不用的含义，可以实现不同的功能

| 指令                      | 作用                                             |
| ------------------------- | ------------------------------------------------ |
| v-for                     | 列表渲染，遍历容器的元素或者对象的属性           |
| v-bind                    | 为HTML标签绑定属性值，如设置`href`、`css`样式等  |
| v-if / v-else / v-else-if | 条件性渲染某元素，判定为`true`时渲染，否则不渲染 |
| v-show                    | 根据条件改变标签`css`的`display`属性值           |
| v-model                   | 在表单元素上创建双向数据绑定                     |
| v-on                      | 为`HTML`标签绑定事件                             |

#### v-for

**标准语法**

```vue
<tr v-for="(item, [index]) in items"[ :key="unique key"]>{{item}}</tr>
```

- `items`：需要遍历的数组对象
- `item`：数组对象中的每个元素
- `index`：当前元素的索引值，从0开始
- `key`：给元素添加的唯一标识，用于提升遍历性能，可以填入某个元素属性来作为标识符

**示例**

```js
employeeList: [
    {
        "id": 1,
        "name": "jack",
        "gender": "male"
    },
    {
        "id": 2,
        "name": "tom",
        "gender": "male"
    },
    {
        "id": 3,
        "name": "lucy",
        "gender": "female"
    }
]
```

如上，在员工管理系统中，事先准备好了一些员工数据，现在需要渲染到前端

有多条员工数据，每个员工独占一行，因此在`tr`标签中添加`v-for`属性遍历员工数组，每个`tr`的`td`处使用插值表达式显示相应的员工数据

```vue
<html>
    <div class="app">
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employeeList" :key="employee.id">
                    <td>{{employee.id}}</td>
                    <td>{{employee.name}}</td>
                    <td>{{employee.gender}}</td>
                </tr>
            </tbody>
        </table> 
    </div>
    <script type="module">
        import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
    
        createApp({
            data() {
                return {
                    employeeList: [
                        {
                            "id": 1,
                            "name": "jack",
                            "gender": "male"
                        },
                        {
                            "id": 2,
                            "name": "tom",
                            "gender": "male"
                        },
                        {
                            "id": 3,
                            "name": "lucy",
                            "gender": "female"
                        }
                    ]
                }
            }
        }).mount(".app")
    </script>
</html>
```

> <img src="./javaweb/2.png">

#### v-bind

**标准语法**

```vue
<img [v-bind]:src="item.image" width="30px">
```

其中`v-bind`可以简化省略

**示例**

```vue
employeeList: [
    {
        "id": 1,
        "name": "jack",
        "gender": "male",
		"avatar": "https://img.pconline.com.cn/images/upload/upc/tx/itbbs/1804/23/c23/84211055_1524470634645_mthumb.jpg"
    },
    {
        "id": 2,
        "name": "tom",
        "gender": "male",
		"avatar": ""
    },
    {
        "id": 3,
        "name": "lucy",
        "gender": "female",
		"avatar": ""
    }
]
```

现在我们需要为每位员工添加一个头像，头像链接已经添加到了`employeeList`数组中

添加`img`标签，为`img`标签的`src`属性设置为`v-bind`属性，通过获取`employee.avatar`来获取员工头像链接

```vue
<html>
    <div class="app">
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Avatar</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employeeList" :key="employee.id">
                    <td>{{employee.id}}</td>
                    <td>{{employee.name}}</td>
                    <td>{{employee.gender}}</td>
                    <td><img :src="employee.avatar" :alt="employee.name" width="50px" height="50px"></td>
                </tr>
            </tbody>
        </table> 
    </div>
    <script type="module">
        import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
    
        createApp({
            data() {
                return {
                    employeeList: [
                        {
                            "id": 1,
                            "name": "jack",
                            "gender": "male",
                            "avatar": "https://ts2.tc.mm.bing.net/th/id/OIP-C.0BBEJKqmDN6MoIm11sRSigHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3"
                        },
                        {
                            "id": 2,
                            "name": "tom",
                            "gender": "male",
                            "avatar": "https://ts3.tc.mm.bing.net/th/id/OIP-C.wYmbI_r2a8cDTJUBJ9HRcgHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3"
                        },
                        {
                            "id": 3,
                            "name": "lucy",
                            "gender": "female",
                            "avatar": ""
                        }
                    ]
                }
            }
        }).mount(".app")
    </script>
</html>
```

> <img src="./javaweb/3.png">

#### v-if

**标准语法**

```vue
<span v-if="conditon1"></span>
<span v-else-if="conditon2"></span>
<span v-else></span>
```

**示例**

```vue
employeeList: [
    {
        "id": 1,
        "name": "jack",
        "gender": "male",
        "avatar": "https://img.pconline.com.cn/images/upload/upc/tx/itbbs/1804/23/c23/84211055_1524470634645_mthumb.jpg",
        "position": 1
    },
    {
        "id": 2,
        "name": "tom",
        "gender": "male",
        "avatar": "https://ts3.tc.mm.bing.net/th/id/OIP-C.wYmbI_r2a8cDTJUBJ9HRcgHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3",
        "position": 2
    },
    {
        "id": 3,
        "name": "lucy",
        "gender": "female",
        "avatar": "",
        "position": 3
    }
]
```

现在每个员工拥有自己的职位，但是在数据库中使用职位号来标识职位，前端需要显示对应的职位名

添加多行`span`，为每个`span`设置一个职位名，通过`v-if`属性来判断当前员工的职位，然后决定是否显示

```vue
<html>
    <div class="app">
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Avatar</th>
                    <th>Position</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employeeList" :key="employee.id">
                    <td>{{employee.id}}</td>
                    <td>{{employee.name}}</td>
                    <td>{{employee.gender}}</td>
                    <td><img :src="employee.avatar" :alt="employee.name" width="50px" height="50px"></td>
                    <td>
                        <span v-if="employee.position==1">boss</span>
                        <span v-else-if="employee.position==2">manager</span>
                        <span v-else>other</span>
                    </td>
                </tr>
            </tbody>
        </table> 
    </div>
    <script type="module">
        import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
    
        createApp({
            data() {
                return {
                    employeeList: [
                        {
                            "id": 1,
                            "name": "jack",
                            "gender": "male",
                            "avatar": "https://img.pconline.com.cn/images/upload/upc/tx/itbbs/1804/23/c23/84211055_1524470634645_mthumb.jpg",
                            "position": 1
                        },
                        {
                            "id": 2,
                            "name": "tom",
                            "gender": "male",
                            "avatar": "https://ts3.tc.mm.bing.net/th/id/OIP-C.wYmbI_r2a8cDTJUBJ9HRcgHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3",
                            "position": 2
                        },
                        {
                            "id": 3,
                            "name": "lucy",
                            "gender": "female",
                            "avatar": "",
                            "position": 3
                        }
                    ]
                }
            }
        }).mount(".app")
    </script>
</html>
```

> <img src="./javaweb/4.png">

#### v-show

**标准语法**

```vue
<span v-show="condition"></span>
```

`v-show`与`v-if`的区别是元素显示逻辑的区别，`v-show`通过更改元素标签的`display`样式来决定是否显示，而`v-if`则是通过是否渲染标签来决定是否显示，在需要频繁更改显示状态的内容中`v-show`更适合

**示例**

使用`v-show`来达到上文员工职位显示的效果

```vue
<html>
    <div class="app">
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Avatar</th>
                    <th>Position</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employeeList" :key="employee.id">
                    <td>{{employee.id}}</td>
                    <td>{{employee.name}}</td>
                    <td>{{employee.gender}}</td>
                    <td><img :src="employee.avatar" :alt="employee.name" width="50px" height="50px"></td>
                    <td>
                        <span v-show="employee.position==1">boss</span>
                        <span v-show="employee.position==2">manager</span>
                        <span v-show="employee.position==3">other</span>
                    </td>
                </tr>
            </tbody>
        </table> 
    </div>
    <script type="module">
        import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
    
        createApp({
            data() {
                return {
                    employeeList: [
                        {
                            "id": 1,
                            "name": "jack",
                            "gender": "male",
                            "avatar": "https://img.pconline.com.cn/images/upload/upc/tx/itbbs/1804/23/c23/84211055_1524470634645_mthumb.jpg",
                            "position": 1
                        },
                        {
                            "id": 2,
                            "name": "tom",
                            "gender": "male",
                            "avatar": "https://ts3.tc.mm.bing.net/th/id/OIP-C.wYmbI_r2a8cDTJUBJ9HRcgHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3",
                            "position": 2
                        },
                        {
                            "id": 3,
                            "name": "lucy",
                            "gender": "female",
                            "avatar": "",
                            "position": 3
                        }
                    ]
                }
            }
        }).mount(".app")
    </script>
</html>
```

<img src="./javaweb/4.png">

#### v-model

 **标准语法**

```vue
<input type="text" id="name" v-model="object.attribute">
```

将用户输入与`object`对象的`attribute`属性绑定

**示例**

```vue
searchForm: {
	name: "",
	gender: "",
	position: ""
}
```

现在需要能够通过员工信息来搜索相应的元素，搜索表单格式如上

新建`form`表单，在表单中创建输入框`input`和选择框`select`，分别设置对应的`v-model`，与`searchForm`对象绑定

```vue
<html>
    <div class="app">
        {{searchForm}}
        <form>
            <label for="name">Name: </label>
            <input type="text" id="name" v-model="searchForm.name">
            &nbsp;&nbsp;
            <label for="gender">Gender: </label>
            <select name="gender" id="gender" v-model="searchForm.gender">
                <option value="">All</option>
                <option value="male">Male</option>
                <option value="female">Female</option>
            </select>
            &nbsp;&nbsp;
            <label for="position">Position: </label>
            <select name="position" id="position" v-model="searchForm.position">
                <option value="">All</option>
                <option value="1">Boss</option>
                <option value="2">Manager</option>
                <option value="3">Other</option>
            </select>
            &nbsp;&nbsp;
            <button type="button">Search</button>
        </form>
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Avatar</th>
                    <th>Position</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employeeList" :key="employee.id">
                    <td>{{employee.id}}</td>
                    <td>{{employee.name}}</td>
                    <td>{{employee.gender}}</td>
                    <td><img :src="employee.avatar" :alt="employee.name" width="50px" height="50px"></td>
                    <td>
                        <span v-if="employee.position==1">boss</span>
                        <span v-else-if="employee.position==2">manager</span>
                        <span v-else-if="employee.position==3">other</span>
                    </td>
                </tr>
            </tbody>
        </table> 
    </div>
    <script type="module">
        import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
    
        createApp({
            data() {
                return {
                    employeeList: [
                        {
                            "id": 1,
                            "name": "jack",
                            "gender": "male",
                            "avatar": "https://img.pconline.com.cn/images/upload/upc/tx/itbbs/1804/23/c23/84211055_1524470634645_mthumb.jpg",
                            "position": 1
                        },
                        {
                            "id": 2,
                            "name": "tom",
                            "gender": "male",
                            "avatar": "https://ts3.tc.mm.bing.net/th/id/OIP-C.wYmbI_r2a8cDTJUBJ9HRcgHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3",
                            "position": 2
                        },
                        {
                            "id": 3,
                            "name": "lucy",
                            "gender": "female",
                            "avatar": "",
                            "position": 3
                        }
                    ],
                    searchForm: {
                        name: "",
                        gender: "",
                        position: ""
                    }
                }
            }
        }).mount(".app")
    </script>
</html>
```

> <img src="./javaweb/5.png">

#### v-on

**标准语法**

```vue
<button type="button" v-on:click="click">click</button>

const app = createApp({
	// 方法对象
	methods: {
		// 具体方法
		click() {
			// 方法体
		}
	}
})
```

可以简写为

```vue
<button type="button" @click="click">click</button>
```

**示例**

现在新建一个重置按钮，为两个按钮绑定事件

使用`v-on`为两个按钮分别绑定`search`和`reset`事件，并在`methods`属性中定义这两个方法

```vue
<html>
    <div class="app">
        <form>
            <label for="name">Name: </label>
            <input type="text" id="name" v-model="searchForm.name">
            &nbsp;&nbsp;
            <label for="gender">Gender: </label>
            <select name="gender" id="gender" v-model="searchForm.gender">
                <option value="">All</option>
                <option value="male">Male</option>
                <option value="female">Female</option>
            </select>
            &nbsp;&nbsp;
            <label for="position">Position: </label>
            <select name="position" id="position" v-model="searchForm.position">
                <option value="">All</option>
                <option value="1">Boss</option>
                <option value="2">Manager</option>
                <option value="3">Other</option>
            </select>
            &nbsp;&nbsp;
            <button type="button" @click="search">Search</button>
            &nbsp;&nbsp;
            <button type="reset">Reset</button>
        </form>
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Avatar</th>
                    <th>Position</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employeeList" :key="employee.id">
                    <td>{{employee.id}}</td>
                    <td>{{employee.name}}</td>
                    <td>{{employee.gender}}</td>
                    <td><img :src="employee.avatar" :alt="employee.name" width="50px" height="50px"></td>
                    <td>
                        <span v-if="employee.position==1">boss</span>
                        <span v-else-if="employee.position==2">manager</span>
                        <span v-else-if="employee.position==3">other</span>
                    </td>
                </tr>
            </tbody>
        </table> 
    </div>
    <script type="module">
        import {createApp} from "https://unpkg.com/vue@3/dist/vue.esm-browser.js"
    
        createApp({
            data() {
                return {
                    employeeList: [
                        {
                            "id": 1,
                            "name": "jack",
                            "gender": "male",
                            "avatar": "https://img.pconline.com.cn/images/upload/upc/tx/itbbs/1804/23/c23/84211055_1524470634645_mthumb.jpg",
                            "position": 1
                        },
                        {
                            "id": 2,
                            "name": "tom",
                            "gender": "male",
                            "avatar": "https://ts3.tc.mm.bing.net/th/id/OIP-C.wYmbI_r2a8cDTJUBJ9HRcgHaLH?pid=ImgDet&w=60&h=60&c=7&rs=1&o=7&rm=3",
                            "position": 2
                        },
                        {
                            "id": 3,
                            "name": "lucy",
                            "gender": "female",
                            "avatar": "",
                            "position": 3
                        }
                    ],
                    searchForm: {
                        name: "",
                        gender: "",
                        position: ""
                    }
                }
            },
            methods: {
                reset() {
                    this.searchForm = {
                        name: "",
                        gender: "",
                        position: ""
                    }
                },
                search() {
                    console.log(this.searchForm)
                }
            }
        }).mount(".app")
    </script>
</html>
```

> <img src="./javaweb/6.png">
