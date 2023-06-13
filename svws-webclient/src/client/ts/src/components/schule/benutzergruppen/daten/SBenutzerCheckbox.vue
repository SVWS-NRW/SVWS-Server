<template>
	<li class="flex items-center gap-0.5 border border-black/25 border-t-0 py-[0.1rem] px-2 min-h-[1.7rem]" :class="{'border-l-0': !spalteLinks}">
		<svws-ui-button type="icon" size="small" title="Benutzer anzeigen" @click.prevent="goToBenutzer(benutzer.id)">
			<i-ri-link />
		</svws-ui-button>
		<label class="cursor-pointer flex flex-1 rounded"
			@mouseover="showIcon=true" @mouseout="showIcon=false"
			@click.prevent="add()"
			:title="spalteLinks ? 'Benutzer zur Gruppe hinzufügen' : 'Benutzer aus Gruppe entfernen'"
			:class="spalteLinks ? 'opacity-50 hover:opacity-100' : 'bg-primary/5 text-primary font-bold hover:bg-transparent'">
			<span class="flex items-center w-full" :class="{'text-error': showIcon && !spalteLinks}">
				<span class="flex-grow inline-flex gap-0.5 items-center">
					{{ benutzer.name }}
					<svws-ui-icon v-if="spalteLinks" v-show="showIcon"> <i-ri-add-circle-fill class="text-primary" /> </svws-ui-icon>
					<svws-ui-icon v-if="!spalteLinks" v-show="showIcon"> <i-ri-close-circle-line /> </svws-ui-icon>
				</span>
				<span class="font-mono inline-flex items-center gap-0.5">{{ benutzer.id }}</span>
			</span>
		</label>
	</li>
</template>

<script setup lang="ts">

	import type {BenutzerListeEintrag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		benutzer: BenutzerListeEintrag;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		goToBenutzer: (b_id: number) => Promise<void>;
	}>();

	const showIcon = ref(false);

	async function add() {
		props.spalteLinks
			? await props.addBenutzerToBenutzergruppe(props.benutzer)
			: await props.removeBenutzerFromBenutzergruppe(props.benutzer)
	}

</script>
