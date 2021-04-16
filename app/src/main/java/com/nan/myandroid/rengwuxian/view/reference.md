## 单位
绘制的时候单位是px像素
## 抗锯齿为什么默认不开启
抗锯齿原理是修改图形以改善显示效果，但其实就不是原来的图形了，就失真了，但是看起来平滑了。
## 在代码里使用dp画图
TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100F,Resources.getSystem().displayMetrics)  
applyDimension 单位转换   
Resources.getSystem().displayMetrics 获取系统的显示指标，也可以通过context.getResources().displayMetrics获取
Resources:资源，包括APP资源：我们APP项目里定义的string drawable 等等，也包括手机系统本身就有的资源，像这里用到的显示指标就是系统Resources 
所以取得时候不需要使用APP的上下文context而直接使用Resources.getSystem获得就可以，以后使用Resources时注意区别。
## Path的填充规则
通过path的addxxx方法组合多个图形，但path其实是一个封闭的路径，是由一个起点和终点，所以会有重叠的部分的舍弃，这就需要填充规则。 
通过path.fillType来设置需要得规则
## Path的测量
PathMeasure.length
PathMeasure.getPosTan  
## 三角函数

## 布局过程
### 测量
### 布局
 

## 触摸反馈
### 原理
简单的自定义View只需要重写`onTouchEvent`方法就可以了。  
返回值代表是否消费这这个事件序列，只在down事件返回有意义，其他事件的时候返回没有作用。
#### `MotionEvent` 的 `getAction` 和 `getActionMasked区别`
多点触控出现之后才出现`getActionMasked`，所以需要判断这次事件是哪个类型的时候使用这个，不要使用`getAction`，因为`getAction`除了包含UP DOWN等类型之后还包含多点触控的第几个点。  
`View`的源码使用的`getAction`是因为最初不支持多点触控，没有`actionMasked`。  

## 多点触控
### 事件序列
1. 点击抬起：ACTION_DOWN ACTION_UP
2. 点击移动抬起：ACTION_DOWN ACTION_MOVE ACTION_MOVE ACTION_MOVE ACTION_MOVE ACTION_UP
3. 多点按下移动抬起：ACTION_DOWN ACTION_MOVE  ACTION_POINTER_DOWN ACTION_MOVE ACTION_MOVE ACTION_POINTER_UP ACTION_UP
### 本质
事件序列针对View而不是手指
单个事件也是针对View而不是手指
一个事件触发的时候，例如：ACTION_POINTER_DOWN，含义并不是这个手指落下了，而是这个View上有一个手指落下了,此时这个事件中有所有手指的信息，而不只是触发当前事件的手指的信息。

一个事件包含的信息有四个：x y index id
index会变化，用来在每个EVENT发生的时候遍历手指（event.getX(index)）。 id不会变化，用来作为点的唯一标识，追踪手指（通过id获取index 再通过index获取相应点的信息）。

getX  getY 获取的是index为0的手指信息 

多点时获取导致事件发生(按下或抬起，正在移动不行)的手指的方法：event.getActionIndex

多点时获取移动事件手指的方法：没有，伪需求，不需要获取。  

UP事件触发的时候，POINTER的个数还没减少，而是在OnTouchEvent执行完之后在减少，注意判断。











