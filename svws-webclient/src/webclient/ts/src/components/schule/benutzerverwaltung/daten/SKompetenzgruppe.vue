<template>
    <div v-if="kompetenzgruppe.daten.id >= 0">
        <thead class="bg-slate-100">
            <tr>
                <td class="border border-[#7f7f7f]/20 text-center">
                    <svws-ui-checkbox v-model="selected"> {{kompetenzgruppe.daten.bezeichnung}} </svws-ui-checkbox>
                </td>
            </tr>
        </thead>
        <tbody>
            <s-kompetenz v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" :kompetenz="kompetenz">
            </s-kompetenz>
        </tbody>
    </div>
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

    const props = defineProps({
        kompetenzgruppe: { type: Object as () => BenutzerKompetenzGruppe, required: true }
    });

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const manager: ComputedRef<BenutzergruppenManager | undefined> = computed(() => {
		return app.dataBenutzergruppe.manager;
	});

    const selected: WritableComputedRef<boolean> = computed({
        get(): boolean {
            return manager.value?.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)) || false;
        },
        set(value: boolean) {
            // TODO
        }
    });

</script>
