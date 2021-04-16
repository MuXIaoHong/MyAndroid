# ViewModel
## 是什么
界面数据的管理类。
## 优势
ViewModel 类旨在以注重生命周期的方式存储和管理界面相关的数据。  
如果系统销毁或重新创建界面控制器，则存储在其中的任何临时性界面相关数据都会丢失。ViewModel 类让数据可在发生屏幕旋转等配置更改后继续留存。
## 为什么会有这样的优势

## 如何使用
见代码
## 注意事项
1. ViewModel不能引用View、LifeCycle或者可能存储了Activity引用的任何类，因为ViewModel 对象存在的时间比视图或 LifecycleOwners 的特定实例存在的时间更长。  
view引用了LifecycleOwners的特定实例（AppCompatActivity），LifeCycle存在于LifecycleOwners中，引用了LifeCycle会导致LifecycleOwners（AppCompatActivity）无法释放。
2. ViewModel 对象绝不能观察对生命周期感知型可观察对象（如 LiveData 对象）的更改。  
在观察LiveData对象时，需要传入lifeCycleOwner对象，就意味着ViewModel中要有lifeCycleOwner对象,这种行为是禁止的，在1中已经讲过了。
3. 如果ViewModel想使用Application实例又想使用默认Factory，可以让ViewModel继承AndroidViewModel
4. 想在ViewModel构造函数传入值的时候需要重写ViewModelProvider.Factory而不能直接调用ViewModel构造器