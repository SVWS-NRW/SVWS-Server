<template>
    <tr>
        <td>
			<svws-ui-checkbox v-model="selected" :disabled="istAlle"> {{ bgle.id}}-{{ bgle.bezeichnung }} </svws-ui-checkbox>
        </td>
    </tr>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
import { Benutzergruppe } from "~/apps/schule/benutzerverwaltung/Benutzergruppe";

    const props = defineProps({
        bgle: { type: Object as () => BenutzergruppeListeEintrag, required: true },
        istAlle:{type:Boolean, default:false}
    });

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	});

    const selected: WritableComputedRef<boolean | undefined> = computed({
        get(): boolean | undefined{
            return manager.value?.IstInGruppe(props.bgle.id);
        },
        set(value: boolean | undefined) {
            if (value)
                 app.dataBenutzer.addBenutzergruppeBenutzer(props.bgle.id); 
            else
                 app.dataBenutzer.removeBenutzergruppeBenutzer(props.bgle.id);
        }
    });

</script>
