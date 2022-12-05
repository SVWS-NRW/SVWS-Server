<template>
    <tr>
        <td>
			<svws-ui-checkbox v-model="selected" :disabled="istAdmin"> {{ kompetenz.daten.id}}-{{ kompetenz.daten.bezeichnung }} </svws-ui-checkbox>
        </td>
    </tr>
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerKompetenz } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

    const props = defineProps({
        kompetenz: { type: Object as () => BenutzerKompetenz, required: true },
        istAdmin:{type:Boolean, required: true}
    });

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const manager: ComputedRef<BenutzergruppenManager | undefined> = computed(() => {
		return app.dataBenutzergruppe.manager;
	});

    const selected: WritableComputedRef<boolean> = computed({
        get(): boolean {
            return manager.value?.hatKompetenz(props.kompetenz) || false;
        },
        set(value: boolean) {
            const alt : boolean = manager.value?.hatKompetenz(props.kompetenz) || false;
            if (alt === value)
                return;
            if (value)
                app.dataBenutzergruppe.addKompetenz(props.kompetenz);
            else
                app.dataBenutzergruppe.removeKompetenz(props.kompetenz);
        }
    });

</script>
