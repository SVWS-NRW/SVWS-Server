<template>
    <div v-if="kompetenzgruppe.daten.id >= 0">
        <thead class="bg-slate-100">
            <tr>
                <td class="border border-[#7f7f7f]/20 text-center">
                    <svws-ui-checkbox v-model="selected" :disabled="istAdmin"> {{kompetenzgruppe.daten.bezeichnung}} </svws-ui-checkbox>
                </td>
            </tr>
        </thead>
        <tbody>
            <s-kompetenz v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" :key="kompetenz.daten.id" :kompetenz="kompetenz" :istAdmin="istAdmin">
            </s-kompetenz>
        </tbody>
    </div>
</template>
<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

    
    const props = defineProps({
        kompetenzgruppe: { type: Object as () => BenutzerKompetenzGruppe, required: true },
        istAdmin:{type:Boolean, required: true},
        benutzertyp : {type : Number, default:0}
    });

	const main: Main = injectMainApp();
	const app_b =  main.apps.benutzer;
    const app_bg =  main.apps.benutzergruppe;

    const manager: ComputedRef<BenutzergruppenManager | BenutzerManager| undefined> = computed(() => {
		return props.benutzertyp === 0 ? app_b.dataBenutzer.manager : app_bg.dataBenutzergruppe.manager;
	});

    const selected: WritableComputedRef<boolean> = computed({
        get(): boolean {
            return manager.value?.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe)) || false;
        },
        set(value: boolean) {
            console.log(props.benutzertyp);
            console.log(manager.value);
            // TODO
            if (value)
                 props.benutzertyp === 0 ? console.log("TO DO") : app_bg.dataBenutzergruppe.addBenutzerKompetenzGruppe(props.kompetenzgruppe) 
            else
                 props.benutzertyp === 0 ? console.log("TO OD") : app_bg.dataBenutzergruppe.removeBenutzerKompetenzGruppe(props.kompetenzgruppe)
        }
    });

</script>
