<template>
	<svws-ui-content-card title="Stundenplan">
		<svws-ui-multi-select
			v-model="selected_stundenplan"
			:items="liste"
			:item-text="(i: StundenplanListeEintrag)=>i.bezeichnung"
			/>
	</svws-ui-content-card>
	<div v-if="stundenplan">{{stundenplan.bezeichnungStundenplan}}</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, Ref, ref, watch, WritableComputedRef } from "vue";

	import { SchuelerStundenplan, StundenplanListeEintrag } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	
	const liste: WritableComputedRef<StundenplanListeEintrag[]> = computed(() => {
			console.log(app.listStundenplaene.liste)
			return app.listStundenplaene.liste
	});

	const stundenplan: ComputedRef<SchuelerStundenplan|undefined> = computed(()=>{
		console.log(app.dataStundenplan.daten)
		return app.dataStundenplan.daten
	})

	const selected_stundenplan: Ref<StundenplanListeEintrag|undefined> = ref(liste.value[0])
	watch(selected_stundenplan, async (neu) => {
		app.listStundenplaene.ausgewaehlt = neu
	}, {immediate: true})
</script>
