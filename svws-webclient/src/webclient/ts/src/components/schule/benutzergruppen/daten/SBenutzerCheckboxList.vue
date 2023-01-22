<template>
	<section v-show="benutzerListe?.length" class="w-full ">
		<div class="font-bond mb-1  center bg-gray-200 rounded-lg flex justify-between  items-center">
			<h1 class="ml-2 font-bold">
				{{ title }}
			</h1>
			<svws-ui-icon v-if="icon"> <i-ri-user-add-line /> </svws-ui-icon>
			<svws-ui-icon v-if="!icon"> <i-ri-user-unfollow-line /> </svws-ui-icon>
		</div>
		<ul class=" divide-y divide-gray-100 overflow-y-auto h-fit">
			<template v-for="benutzer in benutzerListe" :key="benutzer.id">
				<s-benutzer-checkbox :benutzer="benutzer" v-model="aktiv" :icon="icon" :data="data" />
			</template>
		</ul>
		<slot />
	</section>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
		benutzerListe : BenutzerListeEintrag[];
		title : string;
		icon : boolean;
	}>();

	const aktiv: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {
			// TODO
		}
	});

</script>
