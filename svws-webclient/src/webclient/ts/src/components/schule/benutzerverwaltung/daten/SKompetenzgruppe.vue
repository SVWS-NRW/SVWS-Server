<template>
    <template v-if="collapsed">
       <tr v-if="collapsed" style="background-color:lightblue; ">
            <td>
                <svws-ui-icon><i-ri-arrow-right-s-line @click="setCollapse()"/></svws-ui-icon>
            </td>
            <td colspan="2"> 
                {{ kompetenzgruppe.daten.bezeichnung }} 
            </td>
            <td>
                <svws-ui-checkbox v-model="selected" :disabled="istAdmin"/>
            </td>
        </tr>
    </template>
    <template v-else>    
        <tr  style="background-color:lightblue;  ">
            <td style="vertical-align: top;" :rowspan="BenutzerKompetenz.getKompetenzen(kompetenzgruppe).size()+1"> 
                <svws-ui-icon><i-ri-arrow-down-s-line @click="collapsed = !collapsed"/></svws-ui-icon> 
            </td>
            <td colspan="2"> {{ kompetenzgruppe.daten.bezeichnung }} </td>
            <td><svws-ui-checkbox v-model="selected" :disabled="istAdmin"/></td>
        </tr>
        <s-kompetenz v-for="kompetenz in BenutzerKompetenz.getKompetenzen(kompetenzgruppe)" 
            :key="kompetenz.daten.id" :kompetenz="kompetenz" :istAdmin="istAdmin" :benutzertyp="benutzertyp">
        </s-kompetenz>
    </template>
        
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { ref, Ref, computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

    
    const props = defineProps({
        kompetenzgruppe: { type: Object as () => BenutzerKompetenzGruppe, required: true },
        istAdmin: { type: Boolean, required: true },
        benutzertyp : {type : Number, default:0 }
    });

	const main: Main = injectMainApp();
	const app_b = main.apps.benutzer;
    const app_bg =  main.apps.benutzergruppe;

    const collapsed: Ref<boolean> = ref(false);

    const manager: ComputedRef<BenutzergruppenManager | BenutzerManager| undefined> = computed(() => {
		return (props.benutzertyp === 0) ? app_b.dataBenutzer.manager : app_bg.dataBenutzergruppe.manager;
	});

    const selected: WritableComputedRef<boolean> = computed({
        get(): boolean {
            if (manager.value === undefined)
                return false;
            return manager.value.hatKompetenzen(BenutzerKompetenz.getKompetenzen(props.kompetenzgruppe));
        },
        set(value: boolean) {
            if (value) {
                if (props.benutzertyp === 0) 
                    app_b.dataBenutzer.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
                else
                    app_bg.dataBenutzergruppe.addBenutzerKompetenzGruppe(props.kompetenzgruppe);
            } else {
                if (props.benutzertyp === 0)
                    app_b.dataBenutzer.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
                else
                    app_bg.dataBenutzergruppe.removeBenutzerKompetenzGruppe(props.kompetenzgruppe);
            }
        }
    });

    function setCollapse(){
        collapsed.value = !collapsed.value;
    }

</script>
