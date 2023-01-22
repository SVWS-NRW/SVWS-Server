<template>
	<div v-if="visible" class="flex flex-row gap-6">
		<div class="w-2/5">
			<s-card-benutzer-daten :data="data" />
			<s-card-benutzer-gruppen-liste :data="data" :benutzergruppen="benutzergruppen" />
		</div>
		<div class="w-3/5">
			<s-card-benutzer-kompetenzen :data="data" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerListeEintrag } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ShallowRef } from 'vue';
	import { DataBenutzer } from '~/apps/schule/benutzerverwaltung/DataBenutzer';
	import { routeSchuleBenutzer } from '~/router/apps/RouteSchuleBenutzer';

	const props = defineProps<{
		item: ShallowRef<BenutzerListeEintrag | undefined>;
		data: DataBenutzer;
		benutzergruppen: BenutzergruppeListeEintrag[];
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeSchuleBenutzer.hidden()) && (props.item.value !== undefined);
	});

</script>
