/*
 * Демонстрация: сущности из проекта entities обрабатываются проектом app,
 * причём оба проекта добавляют свои части и свои операции в общий контекст.
 */
package org.example.app;

import org.example.context.Entity;
import org.example.context.OperationPipeline;
import org.example.entities.HealOperation;
import org.example.entities.Health;
import org.example.entities.MoveOperation;
import org.example.entities.Name;
import org.example.entities.Poison;
import org.example.entities.PoisonOperation;
import org.example.entities.Position;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Entity hero = new Entity()
            .set(Name.KEY, Name.of("Герой"))
            .set(Health.KEY, Health.of(80, 100))
            .set(Position.KEY, Position.of(0, 0))
            .set(Poison.KEY, Poison.of(5, 2))
            .set(Team.KEY, Team.of("союзники"))
            .set(Buff.KEY, Buff.of(10));

        Entity chest = new Entity()
            .set(Name.KEY, Name.of("Сундук"))
            .set(Position.KEY, Position.of(3, 4))
            .set(Team.KEY, Team.of("нейтральные"));

        Entity orc = new Entity()
            .set(Name.KEY, Name.of("Орк"))
            .set(Health.KEY, Health.of(40, 40))
            .set(Position.KEY, Position.of(10, 0))
            .set(Team.KEY, Team.of("враги"));

        OperationPipeline pipeline = new OperationPipeline()
            .add(new HealOperation(15))   // операция из entities
            .add(new BuffOperation())     // операция из app
            .add(new PoisonOperation())   // операция из entities
            .add(new MoveOperation(2, -1)) // операция из entities
            .add(new TeamReportOperation()); // операция из app

        List<Entity> world = List.of(hero, chest, orc);
        for (int round = 1; round <= 2; round++) {
            System.out.println("=== Ход " + round + " ===");
            pipeline.runAll(world);
        }

        System.out.println("=== Итоговые состояния ===");
        for (Entity entity : world) {
            System.out.println(entity);
        }
    }
}
