package aoc2015.jav;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

class Day22 {

  private static int solve(List<String> s, boolean hard) {
    PriorityQueue<Wizard> wizards = new PriorityQueue<>((a, b) -> Integer.compare(b.manaSpend, a.manaSpend));
    int minManaSpend = Integer.MAX_VALUE;
    List<List<String>> casts = new ArrayList<>();
    wizards.add(new Wizard(50, 500, parseBoss(s)));
    while (wizards.size() > 0) {
      Wizard curr = wizards.poll();
      if (hard && --curr.hp <= 0)
        continue;
      curr.applyEffect();
      for (int spell = 0; spell < Wizard.spells.length; spell++) {
        if (curr.canCast(spell)) {
          Wizard next = new Wizard(curr);
          next.castSpell(spell);
          next.applyEffect();
          next.hp -= Math.max(1, next.boss[1] - next.armor);

          if (next.boss[0] <= 0) {
            if (next.manaSpend == minManaSpend)
              casts.add(next.casts);
            else if (next.manaSpend < minManaSpend) {
              minManaSpend = next.manaSpend;
              casts.clear();
              casts.add(next.casts);
            }
          } else if (next.hp > 0 && next.manaSpend < minManaSpend)
            wizards.add(next);
        }
      }
    }
    casts.forEach(System.out::println);
    return minManaSpend;
  }

  private static int[] parseBoss(List<String> s) {
    int[] boss = new int[2];
    for (int i = 0; i < boss.length; i++)
      boss[i] = Integer.valueOf(s.get(i).split(": ")[1]);
    return boss;
  }

  public static void main(String[] args) throws IOException {
    List<String> s = Files.readAllLines(Paths.get("./input/2015/Day22_input.txt"));
    System.out.println("Part One = " + solve(s, false));
    System.out.println("Part Two = " + solve(s, true));
  }
}

class Wizard {

  static final int[][] spells = {{53, 0}, {73, 0}, {113, 6}, {173, 6}, {229, 5}};
  private static final String[] names = {"Magic Missile", "Drain", "Shield", "Poison", "Recharge"};
  final List<String> casts = new ArrayList<>();
  final int[] boss; // {hp, dmg}
  int hp;
  int armor;
  int manaSpend;
  private int mana;
  private int[] active_effects = new int[5];

  public Wizard(int hp, int mana, int[] boss) {
    this.hp = hp;
    this.mana = mana;
    this.boss = boss;
  }

  public Wizard(Wizard old) {
    this(old.hp, old.mana, old.boss.clone());
    this.manaSpend = old.manaSpend;
    this.active_effects = old.active_effects.clone();
    this.casts.addAll(old.casts);
  }

  boolean canCast(int i) {
    return mana >= spells[i][0] && active_effects[i] == 0;
  }

  void castSpell(int i) {
    casts.add(names[i]);
    mana -= spells[i][0];
    manaSpend += spells[i][0];
    active_effects[i] = spells[i][1];
    if (i == 0) { // Magic Missile
      boss[0] -= 4;
    } else if (i == 1) { // Drain
      hp += 2;
      boss[0] -= 2;
    }
  }

  void applyEffect() {
    for (int i = 0; i < active_effects.length; i++) {
      if (active_effects[i] > 0) {
        active_effects[i]--;
        switch (i) {
          case 2:  // activate Shield
            armor = 7;
            break;
          case 3:  // Poison
            boss[0] -= 3;
            break;
          case 4:  // Recharge
            mana += 101;
            break;
        }
      } else if (i == 2) // deactivate Shield
        armor = 0;
    }
  }
}
