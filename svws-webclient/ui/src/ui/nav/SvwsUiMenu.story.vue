<template>
	<Story title="Sidebar/Menu" id="svws-ui-menu" :layout="{ type: 'single', iframe: false }" group="app">
		<Variant title="Default" id="default">
			<svws-ui-menu>
				<template #header>
					<svws-ui-menu-header>SVWS-NRW</svws-ui-menu-header>
				</template>
				<template #default>
					<svws-ui-menu-item icon="team" active>Schüler</svws-ui-menu-item>
					<svws-ui-menu-item icon="building">Schule</svws-ui-menu-item>
					<svws-ui-menu-item icon="book-open">Kataloge</svws-ui-menu-item>
					<svws-ui-menu-item icon="donut-chart">Extras</svws-ui-menu-item>
				</template>
				<template #footer>
					<svws-ui-menu-item icon="settings-2">Einstellungen</svws-ui-menu-item>
					<svws-ui-menu-item icon="logout-box" subline="Schule XYZ">Administrator</svws-ui-menu-item>
				</template>
			</svws-ui-menu>
		</Variant>
	</Story>
</template>
