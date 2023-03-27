<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="returnToSchule">Schule</a>
				<span title="Benutzer">Datenaustausch</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
					<svws-ui-menu-item @click="setChild(child_item)" :active="child.name?.toString().split('.')[2] === child_item.name.toString().split('.')[2]">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
				</template>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { SchuleDatenaustauschAuswahlProps } from './SSchuleDatenaustauschAuswahlProps';

	const props = defineProps<SchuleDatenaustauschAuswahlProps>();

</script>
