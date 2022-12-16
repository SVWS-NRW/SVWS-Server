<template>
    <tr>
        <td>
			<svws-ui-checkbox v-model="selected" :disabled="aktiviert"> {{ kompetenz.daten.id}}-{{ kompetenz.daten.bezeichnung }} </svws-ui-checkbox>
        </td>
    </tr>
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerKompetenz, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

    const props = defineProps({
        kompetenz: { type: Object as () => BenutzerKompetenz, required: true },
        istAdmin:{ type: Boolean, required: true },
        benutzertyp : {type : Number, default:0 }
    });

	const main : Main = injectMainApp();
	const app_b = main.apps.benutzer;
    const app_bg =  main.apps.benutzergruppe;

	const manager : ComputedRef<BenutzergruppenManager | BenutzerManager| undefined> = computed(() => {
		return (props.benutzertyp === 0) ? app_b.dataBenutzer.manager : app_bg.dataBenutzergruppe.manager;
	});


    const aktiviert : ComputedRef<Boolean | undefined> = computed(() => {
        if (props.benutzertyp === 0){
            return manager.value?.istAdmin() || app_b.dataBenutzer.manager?.getGruppen(props.kompetenz).size() !== 0;
        } else{
            return props.istAdmin;
        }
    });

    const selected: WritableComputedRef<boolean> = computed({
        get(): boolean {
            if (manager.value === undefined)
                return false;
            return manager.value.hatKompetenz(props.kompetenz);
        },
        set(value: boolean) {
            if (props.benutzertyp === 0) {
                if (value)
                    app_b.dataBenutzer.addKompetenz(props.kompetenz);
                else
                    app_b.dataBenutzer.removeKompetenz(props.kompetenz);
            } else {
                const alt : boolean = manager.value?.hatKompetenz(props.kompetenz) || false;
                if (alt === value)
                    return;
                if (value)
                    app_bg.dataBenutzergruppe.addKompetenz(props.kompetenz);
                else
                    app_bg.dataBenutzergruppe.removeKompetenz(props.kompetenz);
            }        
        }
    });

</script>
