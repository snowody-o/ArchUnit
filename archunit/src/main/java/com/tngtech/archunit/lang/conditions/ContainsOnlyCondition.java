/*
 * Copyright 2017 TNG Technology Consulting GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tngtech.archunit.lang.conditions;

import java.util.Collection;

import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.CollectsLines;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.conditions.ContainAnyCondition.AnyConditionEvent;

class ContainsOnlyCondition<T> extends ArchCondition<Collection<? extends T>> {
    private final ArchCondition<T> condition;

    ContainsOnlyCondition(ArchCondition<T> condition) {
        super("contain only elements that " + condition.getDescription());
        this.condition = condition;
    }

    @Override
    public void check(Collection<? extends T> collection, ConditionEvents events) {
        ConditionEvents subEvents = new ConditionEvents();
        for (T item : collection) {
            condition.check(item, subEvents);
        }
        if (!subEvents.isEmpty()) {
            events.add(new OnlyConditionEvent(collection, subEvents));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{condition=" + condition + "}";
    }

    static class OnlyConditionEvent implements ConditionEvent {
        private final Collection<?> correspondingObjects;
        private final Collection<ConditionEvent> allowed;
        private final Collection<ConditionEvent> violating;

        private OnlyConditionEvent(Collection<?> correspondingObjects, ConditionEvents events) {
            this(correspondingObjects, events.getAllowed(), events.getViolating());
        }

        OnlyConditionEvent(Collection<?> correspondingObjects,
                Collection<ConditionEvent> allowed,
                Collection<ConditionEvent> violating) {
            this.correspondingObjects = correspondingObjects;
            this.allowed = allowed;
            this.violating = violating;
        }

        @Override
        public boolean isViolation() {
            return !violating.isEmpty();
        }

        @Override
        public void addInvertedTo(ConditionEvents events) {
            events.add(new AnyConditionEvent(correspondingObjects, violating, allowed));
        }

        @Override
        public void describeTo(CollectsLines messages) {
            messages.add(EventsDescription.describe(violating));
        }

        @Override
        public void handleWith(Handler handler) {
            for (ConditionEvent event : violating) {
                event.handleWith(handler);
            }
        }
    }
}
