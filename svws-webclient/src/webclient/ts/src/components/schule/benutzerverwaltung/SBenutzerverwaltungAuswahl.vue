<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="router.push({ name: name })"
				/>Benutzerverwaltung
			</div>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container mt-2">
				<svws-ui-sidebar-menu-item
					v-for="item in menu_items"
					:key="item.value"
					@click="router.push({ name: item.value })"
				>
					<template #label>
						<span>{{ item.title }}</span>
					</template>
				</svws-ui-sidebar-menu-item>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>
<script setup lang="ts">
	import { ref } from "vue";
	import { router } from "~/router";
	const name = ref("schule");
	const menu_items = [
		{ title: "Benutzer", value: "benutzer" },
		{ title: "Benutzergruppe", value: "benutzergruppe" }
	];
</script>
