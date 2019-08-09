## 前言
使用了[ratel](https://github.com/ainilili/ratel) dev-1.1.0分支 的大量代码，做了一个和斗地主类似的棋牌游戏

## 游戏名：犬決犬
### 1
可以出的牌型和斗地主一样，比如单张，对子，顺子等。
没有地主和农民的区别，每人18张牌，持有方块3的先出牌，先把手牌打完的玩家判为输方，游戏结束。

### 2
每回合的第一个出牌的玩家需要遵循以下规则：

- 如果当前玩家的手牌中有全部玩家的手牌的最小的单张牌，则必须要打出至少包含一张这种牌的牌型
例如开局时，全部玩家的手牌的最小的单张牌是3，如果当前玩家的手牌是：
33445556...
则该玩家可以出单张3、一对3，三张5带个3等。

- 如果当前玩家的手牌中没有全部玩家的手牌的最小的单张牌，则打出的牌必须是该玩家手牌中这个牌型里最小的
如果该玩家的手牌是：
4455566677...
该玩家的手牌中：
最小的单张是4
最小的对子是44
最小的三带是555等等。

- 如果当前玩家打出的牌包括了至少一张该玩家手牌中的最小牌，则可以无视以上两条规则。
如果该玩家的手牌是：
4455566677...
该玩家最小的三带是555，但是如果打出的牌是三张6带个4也是可以了。

### 3
每回合的第一个玩家出牌之后，之后每个玩家的手牌如果有大于上家出的牌则必须要出。